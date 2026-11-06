<script>
import draggable from 'vuedraggable'
import render from '@/utils/generator/render'

const components = {
    itemBtns(h, element, index, parent) {
        const { copyItem, deleteItem, editLogical } = this.$listeners
        let copyBtn = <span class="drawing-item-copy" title="复制" onClick={event => {
            copyItem(element, parent, index); event.stopPropagation()
        }}>
            <i class="el-icon-copy-document" />
        </span>
        let delBtn = <span class="drawing-item-delete" title="删除"
            onClick={event => { deleteItem(index, parent); event.stopPropagation() }}>
            <i class="el-icon-delete" />
        </span>
        let editBtn = <span class="drawing-item-logic" title="逻辑设置" onClick={event => {
            editLogical(element, parent); event.stopPropagation()
        }}>
            <i class="el-icon-setting" />
        </span>
        let btnList = [{ dom: copyBtn, name: 'copy' }, { dom: editBtn, name: 'edit' }, { dom: delBtn, name: 'del' }]

        if (element.isHideCopyBtn) {
            btnList = btnList.filter(f => f.name != 'copy')
        }
        if (element.isHideDelBtn) {
            btnList = btnList.filter(f => f.name != 'del')
        }
        if (element.isHideEditBtn) {
            btnList = btnList.filter(f => f.name != 'edit')
        }
        return btnList.map(m => m.dom)
    }
}
const layouts = {
    colFormItem(h, element, index, parent) {
        // console.log('colFormItem',h, element)
        const { activeItem, editItem, setInstallmentRules } = this.$listeners
        let className = this.activeId === element.propertyCode ? 'drawing-item active-from-item' : 'drawing-item'
        if (this.formConf.unFocusedComponentBorder) className += ' unfocus-bordered'
        // console.log(element.propertyName)
        return (
            <el-col span={element.span} class={className}
                nativeOnClick={event => { activeItem(element); event.stopPropagation() }}>
                <el-form-item label-width={element.labelWidth ? `${element.labelWidth}px` : null}
                    required={element.required} class={element.isFlex ? '' : 'display-flex'}>
                    {element.assemblyType != 'tool' && element.labelType != 'Caption' && !element.isHideName ?
                        <template slot="label">
                            <div class="colFormItem_label">
                                <el-popover placement="top-start" width="280" trigger="hover"
                                    disabled={element.propertyName.length < 20} content={element.propertyName}>
                                    <el-input slot="reference" v-model={element.propertyName} class="custom_input" />
                                </el-popover>
                                {element.isShowSubTitle ?
                                    <el-input type="textarea" v-show="{element.isShowSubTitle}"
                                        placeholder="请输入副标题或者说明内容" v-model={element.subTitle} class="custom_input" />
                                    : ''}
                            </div>
                        </template>
                        : ''}
                    <div class="display-flex render_content">
                        <render key={element.renderKey} conf={element} onInput={event => {
                            this.$set(element, 'defaultValue', event)
                        }} />
                        {element.isShowUnit ?
                            <div class="unit_info">{element.unit} </div> : ''}
                    </div>
                    {element.isEditOption ?
                        <el-button icon="el-icon-plus" type="text"
                            nativeOnClick={event => { editItem(element); event.stopPropagation() }}>选项编辑</el-button>
                        : ''
                    }
                    {element.labelType == 'CaseSpecification' ?
                        <el-button icon="el-icon-plus" type="text"
                            nativeOnClick={event => { setInstallmentRules(element); event.stopPropagation() }}>
                            设置分期规则</el-button>
                        : ''
                    }
                </el-form-item>

                <div class="row_form_itemBtns">
                    {
                        element.isHideBtn ? '' : components.itemBtns.apply(this, arguments)
                    }
                </div>
            </el-col>
        )
    },
    rowFormItem(h, element, index, parent) {
        const { activeItem, draggedMove } = this.$listeners
        const className = this.activeId === element.formCode ? 'drawing-row-item active-from-item' : 'drawing-row-item'
        let child = element.formType == '2' && element.tagType != 'hideForm' ? '' : renderChildren.apply(this, arguments)
        if (!element.span) element.span = 24
        return (
            <el-col span={element.span}>
                <el-row gutter={element.gutter} class={className} type="flex"
                    nativeOnClick={event => { activeItem(element); event.stopPropagation() }}>
                  <el-form-item label-width={element.labelWidth ? `${element.labelWidth}px` : null}
                    required={element.required}>
                    {
                        !element.isHideName ?
                         <template slot='label'>
                            <div class="component-name">
                                <el-popover placement="top-start" width="280" trigger="hover"
                                    disabled={element.formName.length < 20} content={element.formName}>
                                    <el-input slot="reference" v-model={element.formName} class="custom_input" />
                                </el-popover>
                                {element.isShowSubTitle ?
                                    <el-input v-show="{element.isShowSubTitle}" type="textarea"
                                        placeholder="请输入副标题或者说明内容" v-model={element.subTitle} class="custom_input" />
                                    : ''}
                            </div>
                        </template> : ''
                    }
                    {element.formType == '2' && element.tagType != 'hideForm' ? '' :
                        <draggable list={element.children} move={draggedMove} animation={340} group="componentsGroup"
                            class="drag-wrapper">
                            {/* <el-row type={element.type} justify={element.justify} align={element.align}> */}
                            {child}
                            {/* </el-row> */}
                        </draggable>
                    }

                    {element.formType == '2' && element.openImage == '1' && element.tagType != 'hideForm' ?
                        <el-upload class="custom_form_upload" list-type="picture-card" limit={9} action={element.imageAction}
                            accept="image/*">
                            <i slot="default" class="el-icon-plus"></i>
                        </el-upload>
                        : ''
                    }
                   </el-form-item>
                    <div class="row_form_itemBtns">
                        {element.isHideBtn ? ''
                            : components.itemBtns.apply(this, arguments)
                        }
                    </div>
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
        draggable
    },
    props: [
        'element',
        'index',
        'drawingList',
        'activeId',
        'formConf'
    ],
    render(h) {
        const layout = layouts[this.element.layout]
        //console.log('layout-------------',layout,'---------',layouts ,this.element.layout)
        if (layout) {
            return layout.call(this, h, this.element, this.index, this.drawingList)
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
.drawing-item {
    padding: 10px 0;
    overflow: hidden;
}
::v-deep .el-form-item__label {
    display: flex;
    padding: 0;
    line-height: 1.3;
    .colFormItem_label{
        width:100%;
    }
}
::v-deep .custom_input {
    .el-input__inner,
    .el-textarea__inner {
        padding: 0;
        border-color: transparent;
        background: transparent;
        text-indent: 5px;
    }
}
.component-name {
    display: block;
    width: 100%;
}
.drawing-row-item {
    padding: 25px 2px 10px;
    flex-wrap: wrap;
}
.display-flex {
    display: flex;
}

::v-deep .customComponent .el-input__inner {
    border-color: #fff;
    background: transparent;
}
//::v-deep .el-form-item__content{
//    width:100%;
// }
</style>
