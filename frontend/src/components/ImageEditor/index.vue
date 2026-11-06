<template>
    <div style="height:100%;" class="editImgeContent">
        <div class="container">
            <div id="tui-image-editor"></div>
        </div>
        <div class="dialog-bottom">
            <el-button style="margin:0" @click="cancleInfo">取消</el-button>
            <el-button type="primary" @click="saveImg">保存</el-button>
        </div>
    </div>
</template>
<script>
    import {
        base64ToFile,
        setCustomTheme,
        setLocale_zh
    } from './until.js'
    import ImageEditor from 'tui-image-editor'
    import {
        uploadImage
    } from '@/api/cms/article.js'

    export default {
        props: {
            bucketName: {
                type: String,
                default: 'bridge',
            },
            imgUrl: {
                type: String,
                default: ''
            }
        },
        data() {
            return {
                instance: null,
                imgSrc: '',
                // imgUrl: '',
                imgName: '图片',
                imgVisible: true
            };
        },
        mounted() {
            this.init()
        },
        methods: {
            init() {
                this.imgVisible = true
                this.$nextTick(() => {
                    this.instance = new ImageEditor(document.querySelector('#tui-image-editor'), {
                        includeUI: {
                            loadImage: {
                                path: this.imgUrl,
                                name: 'image'
                            },
                            // menu: ['resize', 'crop', 'rotate', 'draw', 'shape', 'icon', 'text',], // 底部菜单按钮列表 隐藏镜像flip和遮罩mask
                            menu: ['shape', 'icon', 'text', ], // 底部菜单按钮列表 隐藏镜像flip和遮罩mask
                            // initMenu: 'draw', // 默认打开的菜单项
                            menuBarPosition: 'bottom', // 菜单所在的位置
                            locale: setLocale_zh(), // 本地化语言为中文
                            theme: setCustomTheme() // 自定义样式

                        },
                        cssMaxWidth: 1000, // canvas 最大宽度
                        cssMaxHeight: 600 // canvas 最大高度

                    })
                    document.getElementsByClassName('tui-image-editor-main')[0].style.top = '50px' // 调整图片显示位置
                    document.getElementsByClassName('tui-image-editor-main')[0].style.background =
                        'rgba(0,0,0,0.5)' // 调整图片显示位置
                    document.getElementsByClassName('tie-btn-history tui-image-editor-item help enabled')[0]
                        .style.display = 'none' // 隐藏顶部历史按钮
                    document.getElementsByClassName('tie-btn-reset tui-image-editor-item help')[0].style
                        .display = 'none' // 隐藏顶部重置按钮
                    document.getElementsByClassName('tie-btn-delete tui-image-editor-item help')[0].style
                        .display = 'none' // 隐藏顶部删除按钮
                    console.log('instance', this.instance)
                })
            },
            // 返回
            cancleInfo() {
                this.$emit('closeeditimg')
            },
            saveImg() {
                const base64String = this.instance.toDataURL();
                const file = base64ToFile(base64String, this.imgName)
                console.log('file', file)
                let params = new FormData()
                params.append('file', file)
                uploadImage(params).then(res => {
                    this.$modal.msgSuccess('图片保存成功')
                })
                this.$emit('closeeditimg', file)

            },

        },
    };
</script>

<style scoped lang="css">
    /* .container {
    height: 90vh;
    text-align: center;
  }

  .dialog-bottom {
    text-align: right;
    padding-top: 20px;
    padding-right: 20px;
  }
  */
    .editImgeContent {
        position: absolute;
        top: 0;
        right: 0;
        left: 0;
        z-index: 999;

    }

    .container {
        height: 100%;
    }

    .dialog-bottom {
        position: absolute;
        top: 8px;
        right: 10px;
        z-index: 999;
        display: flex;
        flex-direction: row;
        padding: 5.5px 0;
    }

    .dialog-bottom .el-button {
        margin-left: 5px;
    }
</style>