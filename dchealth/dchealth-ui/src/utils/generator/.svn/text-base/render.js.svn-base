import { makeMap } from '@/utils/index'

// 参考https://github.com/vuejs/vue/blob/v2.6.10/src/platforms/web/server/util.js
const isAttr = makeMap(
    'accept,accept-charset,accesskey,action,align,alt,async,autocomplete,'
    + 'autofocus,autoplay,autosave,bgcolor,border,buffered,challenge,charset,'
    + 'checked,cite,class,code,codebase,color,cols,colspan,content,http-equiv,'
    + 'name,contenteditable,contextmenu,controls,coords,data,datetime,default,'
    + 'defer,dir,dirname,disabled,download,draggable,dropzone,enctype,method,for,'
    + 'form,formaction,headers,height,hidden,high,href,hreflang,http-equiv,'
    + 'icon,id,ismap,itemprop,keytype,kind,label,lang,language,list,loop,low,'
    + 'manifest,max,maxlength,media,method,GET,POST,min,multiple,email,file,'
    + 'muted,name,novalidate,open,optimum,pattern,ping,placeholder,poster,'
    + 'preload,radiogroup,readonly,rel,required,reversed,rows,rowspan,sandbox,'
    + 'scope,scoped,seamless,selected,shape,size,type,text,password,sizes,span,'
    + 'spellcheck,src,srcdoc,srclang,srcset,start,step,style,summary,tabindex,'
    + 'target,title,type,usemap,value,width,wrap'
)

function vModel(self, dataObject, defaultValue) {
    if (dataObject.props.labelType == "Statistic") {
        dataObject.props.value = 0
    } else {
        dataObject.props.value = defaultValue
    }

    dataObject.on.input = val => {
        self.$emit('input', val)
    }
}

const componentChild = {
    'el-button': {
        default(h, conf, key) {
            return conf[key]
        },
    },
    'el-input': {
        prepend(h, conf, key) {
            return <template slot="prepend">{conf[key]}</template>
        },
        append(h, conf, key) {
            return <template slot="append">{conf[key]}</template>
        }
    },
    'el-select': {
        options(h, conf, key) {
            const list = []
            conf.options.forEach(item => {
                let label = item.label
                if (item.itemScores) { label += '（' + item.itemScores + '分）' }
                list.push(<el-option label={label} value={item.value} disabled={item.disabled}></el-option>)
            })
            return list
        }
    },
    'el-radio-group': {
        options(h, conf, key) {
            const list = []
            conf.options.forEach(item => {
                let disposeStr = item.disposeExplain ? item.disposeExplain.replace(/\n|\r\n/g, '<br>').replace(/ /g, ' &nbsp') : ''
                let label = item.label
                if (item.itemScores) { label += '（' + item.itemScores + '分）' }
                let itemDom = <div class="group_custom_item">
                    {conf.optionType === 'button'
                        ? <el-radio-button label={item.value}>{label}</el-radio-button>
                        : <el-radio label={item.value} border={conf.border}>{label}</el-radio>}
                    {item.inputAttr && item.inputAttr.inputPrefixVal ?
                        <p class="radio_text">{item.inputAttr.inputPrefixVal}</p> : ''}
                    {item.isShowInputValue ? item.inputAttr && item.inputAttr.inputType == 'number' ?
                        <el-input-number value={item.inputAttr.inputDefaultVal} max={item.inputAttr.max}
                            min={item.inputAttr.min} step={item.inputAttr.step} precision={item.inputAttr.precision}
                            controls={false} />
                        : <el-input class="option_input" value={item.inputAttr.inputDefaultVal}
                            placeholder={item.inputAttr.placeholder} />
                        : ''}
                    {item.inputAttr && item.inputAttr.inputSuffixVal ?
                        <p class="radio_text">{item.inputAttr.inputSuffixVal}</p> : ''}
                    {item.isShowInputValue && item.inputAttr && item.inputAttr.unit ?
                        <p class="radio_text">{item.inputAttr.unit}</p> : ''}
                    {item.disposeExplain ?
                        <p class="dispose_explain" domPropsInnerHTML={disposeStr}></p> : ''}
                </div>
                list.push(itemDom)
            })
            return list
        },
    },
    'el-checkbox-group': {
        options(h, conf, key) {
            const list = []
            conf.options.forEach(item => {
                let disposeStr = item.disposeExplain ? item.disposeExplain.replace(/\n|\r\n/g, '<br>').replace(/ /g, ' &nbsp') : ''
                let label = item.label
                if (item.itemScores) { label += '（' + item.itemScores + '分）' }
                let itemDom = <div class="group_custom_item">
                    {conf.optionType === 'button'
                        ? <el-checkbox-button label={item.value} disable={item.disable}>{label}</el-checkbox-button>
                        : <el-checkbox label={item.value} disable={item.disable} border={conf.border}>{label}</el-checkbox>}
                    {item.inputAttr && item.inputAttr.inputPrefixVal ?
                        <p class="radio_text">{item.inputAttr.inputPrefixVal}</p> : ''}
                    {item.isShowInputValue ? item.inputAttr && item.inputAttr.inputType == 'number' ?
                        <el-input-number value={item.inputAttr.inputDefaultVal} max={item.inputAttr.max}
                            min={item.inputAttr.min} step={item.inputAttr.step} precision={item.inputAttr.precision}
                            controls={false} />
                        : <el-input class="option_input" value={item.inputAttr.inputDefaultVal}
                            placeholder={item.inputAttr.placeholder} />
                        : ''}
                    {item.inputAttr && item.inputAttr.inputSuffixVal ?
                        <p class="radio_text">{item.inputAttr.inputSuffixVal}</p> : ''}
                    {item.isShowInputValue && item.inputAttr && item.inputAttr.unit ?
                        <p class="radio_text">{item.inputAttr.unit}</p> : ''}
                    {item.disposeExplain ?
                        <p class="dispose_explain" domPropsInnerHTML={disposeStr}></p> : ''}
                </div>
                list.push(itemDom)
            })
            return list
        }
    },
    'el-upload': {
        'list-type': (h, conf, key) => {
            const list = []
            if (conf['list-type'] === 'picture-card') {
                list.push(<i class="el-icon-plus"></i>)
            } else {
                list.push(<el-button type="primary" icon="el-icon-upload">{conf.buttonText}</el-button>)
            }
            if (conf.showTip) {
                list.push(<div slot="tip" class="el-upload__tip">只能上传不超过 {conf.fileSize}{conf.sizeUnit} 的{conf.accept}文件</div>)
            }
            return list
        }
    },
    // 自定义 矩阵填空
    'matrix': {
        options(h, conf, key) {
            const list = []
            console.log('conf#######################=============', h, conf)
            return conf.children
        },
        type(h, conf, key) {
            return conf.type
        }
    },
    'el-statistic': {
        suffix(h, conf, key) {
            return <template slot="suffix">{conf[key]}</template>

        }
    }
}

export default {
    render(h) {
        const dataObject = {
            attrs: {},
            props: {},
            on: {},
            style: {}
        }
        const confClone = JSON.parse(JSON.stringify(this.conf))
        const children = []

        const childObjs = componentChild[confClone.tag]
        if (childObjs) {
            Object.keys(childObjs).forEach(key => {
                const childFunc = childObjs[key]
                if (confClone[key]) {
                    children.push(childFunc(h, confClone, key))
                }
            })
        }

        Object.keys(confClone).forEach(key => {
            const val = confClone[key]
            if (key === 'vModel') {
                vModel(this, dataObject, confClone.defaultValue)
            } else if (dataObject[key]) {
                dataObject[key] = val
            } else if (!isAttr(key)) {
                dataObject.props[key] = val
            } else {
                dataObject.attrs[key] = val
            }
        })
        return h(this.conf.tag, dataObject, children)
    },
    props: ['conf'],
    methods: {
    }

}
