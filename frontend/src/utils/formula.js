export function evaluateFormulaList(formulaList, flatFields = [], options = {}) {
  const tokenList = Array.isArray(formulaList) ? formulaList : [];
  if (!tokenList.length) {
    return 0;
  }

  const getFieldScore = typeof options.getFieldScore === "function" ? options.getFieldScore : () => 0;
  const applyFunction = typeof options.applyFunction === "function" ? options.applyFunction : defaultApplyFormulaFunction;
  const fieldMap = {};

  if (Array.isArray(flatFields)) {
    flatFields.forEach((field) => {
      if (field && field.propertyCode) {
        fieldMap[field.propertyCode] = field;
      }
    });
  }

  const context = {
    tokenList,
    fieldMap,
    getFieldScore,
    applyFunction,
  };

  const result = parseFormulaAddSub(0, context);
  const numberValue = Number(result.value);
  return Number.isFinite(numberValue) ? numberValue : 0;
}

function parseFormulaAddSub(startIndex, context) {
  let current = parseFormulaMulDiv(startIndex, context);
  let currentIndex = current.index;

  while (currentIndex < context.tokenList.length) {
    const token = context.tokenList[currentIndex];
    if (!token || token.type !== "operator" || !["+", "-"].includes(token.label)) {
      break;
    }
    const next = parseFormulaMulDiv(currentIndex + 1, context);
    current.value = token.label === "+" ? Number(current.value) + Number(next.value) : Number(current.value) - Number(next.value);
    currentIndex = next.index;
  }

  return {
    value: current.value,
    index: currentIndex,
  };
}

function parseFormulaMulDiv(startIndex, context) {
  let current = parseFormulaPrimary(startIndex, context);
  let currentIndex = current.index;

  while (currentIndex < context.tokenList.length) {
    const token = context.tokenList[currentIndex];
    if (!token || token.type !== "operator" || !["*", "/"].includes(token.label)) {
      break;
    }
    const next = parseFormulaPrimary(currentIndex + 1, context);
    current.value = token.label === "*" ? Number(current.value) * Number(next.value) : Number(next.value) === 0 ? 0 : Number(current.value) / Number(next.value);
    currentIndex = next.index;
  }

  return {
    value: current.value,
    index: currentIndex,
  };
}

function parseFormulaPrimary(startIndex, context) {
  const token = context.tokenList[startIndex];
  if (!token) {
    return {
      value: 0,
      index: startIndex,
    };
  }

  if (token.type === "operator" && token.label === "(") {
    const inner = parseFormulaAddSub(startIndex + 1, context);
    return {
      value: inner.value,
      index: context.tokenList[inner.index] && context.tokenList[inner.index].label === ")" ? inner.index + 1 : inner.index,
    };
  }

  if (token.type === "operator" && token.label === "-") {
    const next = parseFormulaPrimary(startIndex + 1, context);
    return {
      value: -Number(next.value),
      index: next.index,
    };
  }

  if (token.type === "operator" && token.label === "+") {
    return parseFormulaPrimary(startIndex + 1, context);
  }

  if (token.type === "function" && token.label && token.label.indexOf("(") > -1) {
    return parseFormulaFunction(startIndex, context);
  }

  if (token.type === "operator" && token.label === ")") {
    return {
      value: 0,
      index: startIndex,
    };
  }

  return {
    value: getFormulaTokenValue(token, context),
    index: startIndex + 1,
  };
}

function parseFormulaFunction(startIndex, context) {
  const token = context.tokenList[startIndex];
  const valueList = [];
  let currentIndex = startIndex + 1;

  while (currentIndex < context.tokenList.length) {
    const currentToken = context.tokenList[currentIndex];
    if (!currentToken) {
      break;
    }
    if (currentToken.label === ")") {
      currentIndex++;
      break;
    }
    if (currentToken.type === "operator" && currentToken.label === "(") {
      const inner = parseFormulaAddSub(currentIndex + 1, context);
      valueList.push(inner.value);
      currentIndex = context.tokenList[inner.index] && context.tokenList[inner.index].label === ")" ? inner.index + 1 : inner.index;
      continue;
    }
    if (currentToken.type === "function" && currentToken.label && currentToken.label.indexOf("(") > -1) {
      const innerFunction = parseFormulaFunction(currentIndex, context);
      valueList.push(innerFunction.value);
      currentIndex = innerFunction.index;
      continue;
    }
    if (currentToken.type === "operator") {
      currentIndex++;
      continue;
    }
    valueList.push(getFormulaTokenValue(currentToken, context));
    currentIndex++;
  }

  return {
    value: context.applyFunction(getFormulaFunctionName(token), valueList),
    index: currentIndex,
  };
}

function getFormulaFunctionName(token) {
  if (token && token.name) {
    return token.name;
  }
  if (token && token.label) {
    return token.label.replace(/\($/, "");
  }
  return "";
}

function getFormulaTokenValue(token, context) {
  if (!token) {
    return 0;
  }

  if (token.propertyCode) {
    const target = context.fieldMap[token.propertyCode];
    if (!target) {
      return 0;
    }
    if (target.labelType === "CaseSpecification") {
      const descriptionValue = Number(target.description);
      return Number.isFinite(descriptionValue) ? descriptionValue : 0;
    }
    const scoreValue = target.labelType === "Statistic" ? target.defaultValue : context.getFieldScore(target);
    const numberValue = Number(scoreValue);
    return Number.isFinite(numberValue) ? numberValue : 0;
  }

  const rawValue = token.value !== undefined && token.value !== null && token.value !== "" ? token.value : token.label;
  const numberValue = Number(rawValue);
  return Number.isFinite(numberValue) ? numberValue : 0;
}

function defaultApplyFormulaFunction(name, valueList) {
  const numbers = valueList.map((item) => Number(item)).filter((item) => Number.isFinite(item));
  if (!numbers.length) {
    return 0;
  }
  if (name === "SUM") {
    return mathSum(numbers);
  }
  if (name === "MAX") {
    return Math.max(...numbers);
  }
  if (name === "MIN") {
    return Math.min(...numbers);
  }
  if (name === "AVERAGE") {
    return mathSum(numbers) / numbers.length;
  }
  if (name === "ROUND") {
    return Math.round(numbers[0]);
  }
  if (name === "PRODUCT") {
    return numbers.reduce((total, value) => total * value, 1);
  }
  if (name === "COUNT") {
    return numbers.length;
  }
  if (name === "MEDIAN") {
    const sorted = numbers.slice().sort((a, b) => a - b);
    const middle = Math.floor(sorted.length / 2);
    return sorted.length % 2 !== 0 ? sorted[middle] : (sorted[middle - 1] + sorted[middle]) / 2;
  }
  if (name === "RANGE") {
    return Math.max(...numbers) - Math.min(...numbers);
  }
  return 0;
}

function mathSum(arr) {
  return arr.reduce((total, value) => total + value, 0);
}
