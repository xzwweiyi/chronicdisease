

<script>
    import render from '@/utils/generator/render'
    import { getToken } from "@/utils/auth";
    
    const layouts = {
    colFormItem(h, element,isShowName) {
        if(element.tag =='el-upload'){
            element['headers'] = {
                Authorization: "Bearer " + getToken()
            }
            element['action'] = process.env.VUE_APP_BASE_API+"/common/upload"
        }
        return (
            <el-col span={element.span} class='drawing-item'>
                <el-form-item label-width={element.labelWidth ? `${element.labelWidth}px` : null}
                    required={element.required} class={element.isFlex?'':'display-flex'} label={isShowName?element.propertyName:''} >
                    <div v-show="{element.isShowSubTitle}" class="custom_input">
                        {element.subTitle}
                    </div>
                    <render key={element.renderKey} conf={element} onInput={ event => {
                        this.$set(element, 'defaultValue', event)
                    }}   />
                
                </el-form-item>
            </el-col>
        )
    },
    rowFormItem(h, element,isShowName) {
        let child = renderChildren.apply(this, arguments,isShowName)
        // console.log('rowFormItem',element.formType,element.tagType,element)

        return (
            <el-col span={element.span}>
                <el-row gutter={element.gutter} type="flex">
                    <div v-show="{element.isShowSubTitle}" class="component-name">
                        {element.subTitle}
                    </div>
                    {element.type === 'flex' ? 
	                <el-row type={element.type} justify={element.justify} align={element.align}>
	                {child}
	                </el-row>
	                : child
	            }
                </el-row>
            </el-col>
        )
    },
}
function renderChildren(h, element, isShowName) {
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
  throw new Error(`没有与${this.info.layout}匹配的layout`)
}

export default {
    components: {
        render,
    },
    props: ['info','isShowName'],
    render(h) {
        const layout = layouts[this.info.layout]
        //console.log('layout-------------',layout,'---------',layouts ,this.info.layout)
        if (layout) {
            return layout.call(this, h, this.info,this.isShowName)
        }
        return layoutIsNotFound()
    },
    methods: {
    }
}

</script>

<style lang="scss" scoped>
    
    ::v-deep .el-form-item__label{
        display: flex;
        padding:0;
        margin-right:5px;
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
    .el-slider {
        width: 80%;
        margin-left: 10px;

        .el-slider__runway.disabled .el-slider__bar {
            background-color: #1890ff;
        }
    }

    .el-image {
        width: 150px;
        height: 100px;
        background: #f5f5f5;
        border: 1px solid #eaeaea;
        margin: 5px;

        .image-slot {
            height: 100%;
            display: flex;
            justify-content: center;
            align-items: center;
        }
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

</style>