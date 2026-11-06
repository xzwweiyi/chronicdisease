<script>
import ComponentEcho from "../components/assembly";

const MedicalFormDescriptions = {
  name: "MedicalFormDescriptions",
  functional: true,
  props: {
    form: {
      type: Object,
      required: true,
    },
    showWarnTag: {
      type: Boolean,
      default: false,
    },
  },
  render(h, context) {
    const { form, showWarnTag } = context.props;

    const getPropertyList = (currentForm) => {
      if (!currentForm) return [];
      if (currentForm.formType == "3" && Array.isArray(currentForm.propertyExamVOS)) {
        return currentForm.propertyExamVOS;
      }
      return Array.isArray(currentForm.propertyVOS) ? currentForm.propertyVOS : [];
    };

    const visibleChildForms = Array.isArray(form.childMedicalFormVOS)
      ? form.childMedicalFormVOS
      : [];
    const visibleProperties = getPropertyList(form).filter(
      (item) => item && item.isShowDom !== false,
    );
    const visibleLibraryForms = Array.isArray(form.libraryFormsList)
      ? form.libraryFormsList
      : [];
    const inlineProperty = visibleProperties.find(
      (item) => item && item.isShowDom !== false,
    );
    const showPropertyExamVOS = form.formType == "3" && !!form.propertyExamVOS;
    const showPropertyVOS = !showPropertyExamVOS;

    const nestedChildren = [];

    visibleChildForms.forEach((childForm, childIndex) => {
      nestedChildren.push(
        h(MedicalFormDescriptions, {
          key: childForm.medicalFormId || `${childForm.medicalCode || "form"}-${childIndex}`,
          props: {
            form: childForm,
          },
        }),
      );
    });

    if (showPropertyVOS) {
      visibleProperties.forEach((propertyItem, propertyIndex) => {
        nestedChildren.push(
          h(
            "el-descriptions-item",
            {
              key:
                propertyItem.id ||
                propertyItem.propertyCode ||
                `${propertyItem.propertyName || "property"}-${propertyIndex}`,
              props: {
                labelClassName: "patient_label",
              },
            },
            [
              h(
                "span",
                { slot: "label" },
                [
                  propertyItem.propertyName,
                  showWarnTag && form.warnSource
                    ? h(
                        "el-tag",
                        {
                          props: {
                            type: "danger",
                            effect: "dark",
                          },
                          style: { marginLeft: "8px" },
                        },
                        ["预警源"],
                      )
                    : null,
                ].filter(Boolean),
              ),
              h(ComponentEcho, {
                props: {
                  info: propertyItem,
                },
              }),
            ],
          ),
        );
      });
    } else {
      visibleProperties.forEach((propertyItem, propertyIndex) => {
        nestedChildren.push(
          h(
            "el-descriptions-item",
            {
              key:
                propertyItem.id ||
                propertyItem.propertyCode ||
                `${propertyItem.propertyName || "property"}-${propertyIndex}`,
              staticClass: "childMedicalFormVOS_property",
              props: {
                labelClassName: "patient_label",
                label: propertyItem.propertyName,
              },
            },
            [
              h(ComponentEcho, {
                props: {
                  info: propertyItem,
                },
              }),
            ],
          ),
        );
      });
    }

    visibleLibraryForms.forEach((libForm, libIndex) => {
      const libraryProperties = Array.isArray(libForm.libraryProperties)
        ? libForm.libraryProperties.filter((item) => item && item.isShowDom !== false)
        : [];

      nestedChildren.push(
        h(
          "el-descriptions-item",
          {
            key:
              libForm.libraryId ||
              libForm.formCode ||
              `${libForm.formName || "library-form"}-${libIndex}`,
            props: {
              labelClassName: "patient_label",
              label: libForm.formName,
            },
          },
          [
            h(
              "el-descriptions",
              {
                props: {
                  column: 1,
                  border: true,
                  direction: "vertical",
                },
              },
              libraryProperties.map((libProp, libPropIndex) =>
                h(
                  "el-descriptions-item",
                  {
                    key:
                      libProp.id ||
                      libProp.propertyCode ||
                      `${libProp.propertyName || "library-property"}-${libPropIndex}`,
                    props: {
                      labelClassName: "patient_label",
                      label: libProp.propertyName,
                    },
                  },
                  [
                    h(ComponentEcho, {
                      props: {
                        info: libProp,
                      },
                    }),
                  ],
                ),
              ),
            ),
          ],
        ),
      );
    });

    if (form.formType == "3" && form.evaluationResult && form.openImage != "1") {
      nestedChildren.push(
        h(
          "el-descriptions-item",
          {
            key: `${form.medicalFormId || form.medicalCode || "form"}-evaluation`,
            staticClass: "childMedicalFormVOS_property",
            props: {
              labelClassName: "patient_label",
              label: "评价结果",
            },
          },
          [h("div", { domProps: { innerHTML: form.evaluationResult } })],
        ),
      );
    }

    const contentNode =
      form.openImage != "1"
        ? h(
            "el-descriptions",
            {
              props: {
                column: 1,
                border: true,
                direction: "vertical",
              },
            },
            nestedChildren,
          )
        : inlineProperty
          ? h("div", { staticClass: "childMedicalFormVOS_property" }, [
              `${inlineProperty.propertyName}：`,
              h(ComponentEcho, {
                props: {
                  info: inlineProperty,
                },
              }),
              form.formType == "3" && form.evaluationResult
                ? h("div", { staticClass: "childMedicalFormVOS_property" }, [
                    "评价结果：",
                    h("div", { domProps: { innerHTML: form.evaluationResult } }),
                  ])
                : null,
            ].filter(Boolean))
          : null;

    return h(
      "el-descriptions-item",
      {
        props: {
          labelClassName: "patient_label",
          label: form.medicalName,
        },
      },
      contentNode ? [contentNode] : [],
    );
  },
};

export default MedicalFormDescriptions;
</script>

<style lang="scss" scoped>
.childMedicalFormVOS_property {
  display: flex;
}
</style>