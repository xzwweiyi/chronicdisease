<script>
import render from '@/utils/generator/render'

const layouts = {
    colFormItem(h, element, index, parent) {
        return (
            <el-col span={element.span} class="drawing-item'">
                <el-form-item label-width={element.labelWidth ? `${element.labelWidth}px` : null}
                    required={element.required} class={element.isFlex ? '' : 'display-flex'}>
                    {element.tagType != 'tool' ?
                        <template slot="label">
                            <div class="colFormItem_label">
                                <el-popover placement="top-start" width="280" trigger="hover"
                                    disabled={element.propertyName.length < 10} content={element.propertyName}>
                                    <el-input slot="reference" v-model={element.propertyName} class="custom_input" />
                                </el-popover>
                                {element.isShowSubTitle ?
                                    <el-input slot="reference" type="textarea" v-show="{element.isShowSubTitle}"
                                        placeholder="请输入副标题或者说明内容" v-model={element.subTitle} class="custom_input" />
                                    : ''}
                            </div>
                        </template>
                        : ''
                    }
                    <render key={element.renderKey} conf={element} onInput={event => {
                        this.$set(element, 'defaultValue', event)
                    }} />
                </el-form-item>
            </el-col>
        )
    },
    rowFormItem(h, element, index, parent) {
        const className = this.activeId === element.propertyCode ? 'drawing-row-item active-from-item' : 'drawing-row-item'
        let child = renderChildren.apply(this, arguments)

        return (
            <el-col span={element.span}>
                <el-row gutter={element.gutter} class={className}>
                    <div class="component-name">
                        <el-input v-model={element.formName} class="custom_input" />
                        {element.isShowSubTitle ? <el-input v-show="{element.isShowSubTitle}"
                            placeholder="请输入副标题或者说明内容" v-model={element.subTitle} class="custom_input" /> : ''}
                    </div>
                    {element.type === 'flex' ?
                        <el-row type={element.type} justify={element.justify} align={element.align}>
                            {child}
                        </el-row>
                        : child
                    }
                    {element.openImage == '1' ?
                        <el-upload class="custom_form_upload" list-type="picture-card" action={element.imageAction} accept="image/*">
                            <i slot="default" class="el-icon-plus"></i>
                        </el-upload>
                        : ''
                    }
                </el-row>
            </el-col>
        )
    },
}

function renderChildren(h, element, index, parent) {
    // console.log('h element',h, element, index, parent )
    if (!Array.isArray(element.children)) return null
    return element.children.map((el, i) => {
        const layout = layouts[el.layout]
        if (layout) {
            return layout.call(this, h, el, i, element.children)
        }
        return layoutIsNotFound()
    })
}

function layoutIsNotFound() {
    throw new Error(`没有与${this.element.layout}匹配的layout`)
}

export default {
    components: {
        render,
    },
    props: [
        'element',
        'index',
        'activeId',
    ],
    render(h) {
        const layout = layouts[this.element.layout]
        if (layout) {
            return layout.call(this, h, this.element, this.index)
        }
        return layoutIsNotFound()
    },
    methods: {
        matrixchange(event) {
            this.$emit('matrixchange', this.voidList)
        }
    }
}
</script>
<style lang='scss' scoped>
    ::v-deep .el-form-item__label{
        display: flex;
        .colFormItem_label{
            width:100%;
        }
    }
    ::v-deep .custom_input .el-input__inner{
        padding:0;
        border-color:transparent;
        background: transparent;
        text-indent: 5px;
    }
    .component-name{
        display: block;
        width: 100%;
    }
    .drawing-row-item{
        padding:25px 2px 10px;
    }
    .display-flex{
        display: flex;
    }

   ::v-deep .customComponent .el-input__inner{
        border-color:#fff;
        background: transparent;
    }
    ::v-deep .el-form-item__content{
        width:100%;
    }
  
    ::v-deep .el-form-item{
        .el-radio-group,
        .el-checkbox-group {
            display: flex;
            flex-wrap:wrap;
            align-items:center;
            min-height: 36px;
            padding:10px 0;
            .group_custom_item{
                display:flex;
                margin-right:10px;
                align-items:center;
                .el-input{
                    width:180px;
                    margin-right: 10px;
                }
                .el-radio,.el-checkbox{
                    margin-right: 10px;
                }
            }
        }
    }
    ::v-deep .el-radio__input.is-disabled + span.el-radio__label,
    ::v-deep .el-checkbox__input.is-disabled + span.el-checkbox__label{
        color:#666;
    }
</style>
