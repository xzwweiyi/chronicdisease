<script>
import MFormInfo from "@/views/tool/build/previewForm/followUpDetailItem";

const DetailsFollowUpNode = {
  name: "DetailsFollowUpNode",
  functional: true,
  props: {
    node: {
      type: Object,
      required: true,
    },
    showWarnTag: {
      type: Boolean,
      default: false,
    },
  },
  render(h, context) {
    const { node, showWarnTag } = context.props;
    const listeners = context.listeners || {};

    if (!node || !node.isShowDom) {
      return null;
    }

    const isFormNode = node.labelType == "form";
    const hasChildren = Array.isArray(node.children) && node.children.length > 0;
    const firstChildNode = hasChildren ? node.children[0] : null;
    const nodeLabel = node.propertyName || node.formName || node.medicalName;

    let contentChildren = [];

    if (isFormNode) {
      if (node.openImage != "1" && hasChildren) {
        contentChildren = [
          h(
            "el-descriptions",
            {
              props: {
                column: 1,
                border: true,
                direction: "vertical",
              },
            },
            node.children.map((childNode, childIndex) =>
              h(DetailsFollowUpNode, {
                key:
                  childNode.renderKey ||
                  childNode.propertyCode ||
                  childNode.formCode ||
                  `${childNode.medicalFormId || "node"}-${childIndex}`,
                props: {
                  node: childNode,
                },
                on: listeners,
              }),
            ),
          ),
        ];
      } else if (node.openImage == "1" && firstChildNode) {
        contentChildren = [
          h("div", { staticClass: "childMedicalFormVOS_property" }, [
            `${firstChildNode.propertyName}：`,
            firstChildNode.isShowSubTitle
              ? h("p", { staticClass: "subTitle_style" }, [firstChildNode.subTitle])
              : null,
            h(MFormInfo, {
              props: {
                info: firstChildNode,
              },
              on: listeners,
            }),
          ].filter(Boolean)),
        ];
      }
    } else {
      contentChildren = [
        node.isShowSubTitle
          ? h("p", { staticClass: "subTitle_style" }, [node.subTitle])
          : null,
        h(MFormInfo, {
          props: {
            info: node,
          },
          on: listeners,
        }),
      ].filter(Boolean);
    }

    return h(
      "el-descriptions-item",
      {
        props: {
          labelClassName: "patient_label",
        },
      },
      [
        h(
          "span",
          { slot: "label" },
          [
            node.required ? h("span", { staticClass: "required" }, ["*"]) : null,
            nodeLabel,
            showWarnTag
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
        ...contentChildren,
      ],
    );
  },
};

export default DetailsFollowUpNode;
</script>