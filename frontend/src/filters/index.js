import moment from 'moment';
import business from './business';
import patient from './patientdetails';

const _checkIsNull = function (val) {
    // isNaN 在检测字符串时候也为true
    // return val == undefined || val === null || isNaN(val) || val.toString().trim() === '';
    // return val == undefined || val === null || val.toString().trim() === '';
    return val === '' || val == null;
}
const _toThounsands = function (num) {
    num = num.toString();
    var result = '';
    while (num.length > 3) {
        result = ',' + num.slice(-3) + result;
        num = num.slice(0, num.length - 3);
    }
    if (num) {
        result = num + result;
    }
    return result;
}

const DefaultNumberFormatOpt = {
    isShortNum: false,
    isThousand: true,
    suffix: '',
    digit: -1
}
export default {
    ...business,
    ...patient,
    percentnuFormat(val, opt) {
        opt = Object.assign({
            emptyPlaceHolder: '--'
        }, opt || {});
        if (!isNaN(val) && !_checkIsNull(val)) {
            var valueFloat = parseFloat(val);
            return val.toString().slice(valueFloat >= 0 ? 0 : 1) + "%";
        }

        return opt.emptyPlaceHolder;
    },

    nullFormat(val, opt) {
        opt = Object.assign({
            emptyPlaceHolder: '--',

        }, opt || {});
        return _checkIsNull(val) ? opt.emptyPlaceHolder : val;
    },

    // numberFormatMain(val, opt) {
    //     opt = Object.assign({
    //         isShortNum: '--',
    //         isThousand
    //         }, opt || {});
    // },
    /**
     * 
     * @param value 需要处理的数字
     * @param isShortNumber 是否进行万或者亿的缩写
     * @param isThousand 是否进行千分位转换
     * @param digit 小数点最大长度
     * @param suffix 后缀
     * 特别注意： 负数不处理
     */
    numberFormat(val, opt) {
        opt = {
            ...DefaultNumberFormatOpt,
            ...(opt || {})
        }
        var result;
        if (!isNaN(val) && !_checkIsNull(val)) {
            var floatVal = parseFloat(val);
            if (floatVal < 0) {
                return val;
            }
            var splitVal = val.toString().split(".");
            var intPart = splitVal[0];
            var decimalPart = '';
            if (splitVal[1]) {
                if (opt.digit > -1) {
                    decimalPart = opt.digit === 0 ? '' : ('.' + splitVal[1].slice(0, opt.digit));
                } else {
                    decimalPart = '.' + splitVal[1];
                }
            }
            if (!opt.isShortNum) {
                result = opt.isThousand ? _toThounsands(intPart) + decimalPart : val;
            } else {
                var intPartLength = intPart.length;

                if (intPartLength > 8) {
                    result = intPart.slice(0, -8)
                    if (opt.isThousand) {
                        result = _toThounsands(result);
                    }
                    if (opt.digit > 0) {
                        result = result + "." + intPart.slice(-8, (intPartLength - 8) + opt.digit);
                    }

                    result = result + '亿';
                } else if (intPartLength > 4) {
                    result = intPart.slice(0, -4)
                    if (opt.isThousand) {
                        result = _toThounsands(result);
                    }
                    if (opt.digit > 0) {

                        result = result + "." + intPart.slice(-4, (intPartLength - 4) + opt.digit);
                    }

                    result = result + '万';
                } else {
                    result = _toThounsands(intPart) + decimalPart;
                }
            }

        } else {
            return '--';
        }

        return result + opt.suffix;
    },

    dateFormat(val, template = 'YYYY-MM-DD HH:mm:ss') {
        if (val !== undefined) {
            const m = moment(val);
            if (m.isValid()) {
                return m.format(template);
            }
        }

        return '--';
    },

}