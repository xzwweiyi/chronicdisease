<template>
    <div>
        <el-upload :action="uploadUrl" :before-upload="handleBeforeUpload" :on-success="handleUploadSuccess"
            :on-error="handleUploadError" name="file" :show-file-list="false" :headers="headers" style="display: none"
            ref="upload" v-if="this.type == 'url'">
        </el-upload>
        <div class="editor" ref="editor" :style="styles"></div>
    </div>
</template>

<script>
import Quill from "quill";
import "quill/dist/quill.core.css";
import "quill/dist/quill.snow.css";
import "quill/dist/quill.bubble.css";
import { uploadImage } from '@/api/cms/article'
import {  getToken } from "@/utils/auth";
// toolbar标题
const titleConfig = [
    { Choice: '.ql-insertMetric', title: '跳转配置' },
    { Choice: '.ql-bold', title: '加粗' },
    { Choice: '.ql-italic', title: '斜体' },
    { Choice: '.ql-underline', title: '下划线' },
    { Choice: '.ql-header', title: '段落格式' },
    { Choice: '.ql-strike', title: '删除线' },
    { Choice: '.ql-blockquote', title: '块引用' },
    { Choice: '.ql-code', title: '插入代码' },
    { Choice: '.ql-code-block', title: '插入代码段' },
    { Choice: '.ql-font', title: '字体' },
    { Choice: '.ql-size', title: '字体大小' },
    { Choice: '.ql-list[value="ordered"]', title: '编号列表' },
    { Choice: '.ql-list[value="bullet"]', title: '项目列表' },
    { Choice: '.ql-direction', title: '文本方向' },
    { Choice: '.ql-header[value="1"]', title: 'h1' },
    { Choice: '.ql-header[value="2"]', title: 'h2' },
    { Choice: '.ql-align', title: '对齐方式' },
    { Choice: '.ql-color', title: '字体颜色' },
    { Choice: '.ql-background', title: '背景颜色' },
    { Choice: '.ql-image', title: '图像' },
    { Choice: '.ql-video', title: '视频' },
    { Choice: '.ql-link', title: '添加链接' },
    { Choice: '.ql-formula', title: '插入公式' },
    { Choice: '.ql-clean', title: '清除字体格式' },
    { Choice: '.ql-script[value="sub"]', title: '下标' },
    { Choice: '.ql-script[value="super"]', title: '上标' },
    { Choice: '.ql-indent[value="-1"]', title: '向左缩进' },
    { Choice: '.ql-indent[value="+1"]', title: '向右缩进' },
    { Choice: '.ql-header .ql-picker-label', title: '标题大小' },
    { Choice: '.ql-header .ql-picker-item[data-value="1"]', title: '标题一' },
    { Choice: '.ql-header .ql-picker-item[data-value="2"]', title: '标题二' },
    { Choice: '.ql-header .ql-picker-item[data-value="3"]', title: '标题三' },
    { Choice: '.ql-header .ql-picker-item[data-value="4"]', title: '标题四' },
    { Choice: '.ql-header .ql-picker-item[data-value="5"]', title: '标题五' },
    { Choice: '.ql-header .ql-picker-item[data-value="6"]', title: '标题六' },
    { Choice: '.ql-header .ql-picker-item:last-child', title: '标准' },
    { Choice: '.ql-size .ql-picker-item[data-value="small"]', title: '小号' },
    { Choice: '.ql-size .ql-picker-item[data-value="large"]', title: '大号' },
    { Choice: '.ql-size .ql-picker-item[data-value="huge"]', title: '超大号' },
    { Choice: '.ql-size .ql-picker-item:nth-child(2)', title: '标准' },
    { Choice: '.ql-align .ql-picker-item:first-child', title: '居左对齐' },
    { Choice: '.ql-align .ql-picker-item[data-value="center"]', title: '居中对齐' },
    { Choice: '.ql-align .ql-picker-item[data-value="right"]', title: '居右对齐' },
    { Choice: '.ql-align .ql-picker-item[data-value="justify"]', title: '两端对齐' }
]


export default {
    name: "Editor",
    props: {
        /* 编辑器的内容 */
        value: {
            type: String,
            default: "",
        },
        /* 高度 */
        height: {
            type: Number,
            default: 400,
        },
        /* 最小高度 */
        minHeight: {
            type: Number,
            default: null,
        },
        /* 只读 */
        readOnly: {
            type: Boolean,
            default: false,
        },
        // 上传文件大小限制(MB)
        fileSize: {
            type: Number,
            default: 5,
        },
        /* 类型（base64格式、url格式） */
        type: {
            type: String,
            default: "url",
        },
        toolbar: {
            default: () => [
                ["bold", "italic", "underline", "strike"], // 加粗 斜体 下划线 删除线
                // ["blockquote", "code-block"], // 引用  代码块
                [{
                    list: "ordered"
                }, {
                    list: "bullet"
                }], // 有序、无序列表
                [{
                    indent: "-1"
                }, {
                    indent: "+1"
                }], // 缩进
                [{
                    size: ["small", false, "large", "huge"]
                }], // 字体大小
                [{
                    header: [1, 2, 3, 4, 5, 6, false]
                }], // 标题
                [{
                    color: []
                }, {
                    background: []
                }], // 字体颜色、字体背景颜色
                [{
                    align: []
                }], // 对齐方式
                ["clean"], // 清除文本格式
                // ["link", "image", "video"] // 链接、图片、视频
                ["image"] // 链接、图片、视频
            ],
        }
    },
    data() {
        return {
            uploadUrl: process.env.VUE_APP_BASE_API + "/common/upload", // 上传的图片服务器地址
            headers: {
                Authorization: "Bearer " + getToken()
            },
            Quill: null,
            currentValue: "",
            options: {
                theme: "snow",
                bounds: document.body,
                debug: "warn",
                modules: {
                    // 工具栏配置
                    toolbar: this.toolbar
                },
                placeholder: "请输入内容",
                readOnly: this.readOnly,
            },
        };
    },
    computed: {
        styles() {
            let style = {};
            if (this.minHeight) {
                style.minHeight = `${this.minHeight}px`;
            }
            if (this.height) {
                style.height = `${this.height}px`;
            }
            return style;
        },
    },
    watch: {
        value: {
            handler(val) {
                if (val !== this.currentValue) {
                    this.currentValue = val === null ? "" : val;
                    if (this.Quill) {
                        let delta = this.Quill.clipboard.convert({
                            html: val
                        });

                        // 编辑器的内容需要接收delta对象
                        // this.Quill.setContents(delta);
                        this.Quill.clipboard.dangerouslyPasteHTML(0, this.currentValue);
                    }
                }
            },
            immediate: true,
        },
    },
    mounted() {
        this.init();
        this.$nextTick(() => {
            this.initTitle();
        });
    },
    beforeDestroy() {
        this.Quill = null;
    },
    methods: {
        initTitle() {
            for (let item of titleConfig) {
                let tip = document.querySelector('.my_editor .ql-snow .ql-formats ' + item.Choice)
                if (!tip) continue
                tip.setAttribute('title', item.title)
            }
        },
        init() {
            if (this.toolbar) {
                this.options.modules.toolbar = this.toolbar
            }
            const editor = this.$refs.editor;
            this.Quill = new Quill(editor, this.options);
            // 如果设置了上传地址则自定义图片上传事件
            if (this.type == 'url') {
                let toolbar = this.Quill.getModule("toolbar");
                toolbar.addHandler("image", (value) => {
                    this.uploadType = "image";
                    if (value) {
                        this.$refs.upload.$children[0].$refs.input.click();
                    } else {
                        this.Quill.format("image", false);
                    }
                });
            }
            this.Quill.root.addEventListener("paste", this.pastHandle.bind(this), true);
            this.Quill.clipboard.dangerouslyPasteHTML(0, this.currentValue);
            this.Quill.on("text-change", (delta, oldDelta, source) => {
                const html = this.$refs.editor.children[0].innerHTML;
                const text = this.Quill.getText();
                const quill = this.Quill;
                this.currentValue = html;
                this.$emit("input", html);
                this.$emit("on-change", { html, text, quill });
            });
            this.Quill.on("text-change", (delta, oldDelta, source) => {
                this.$emit("on-text-change", delta, oldDelta, source);
            });
            this.Quill.on("selection-change", (range, oldRange, source) => {
                this.$emit("on-selection-change", range, oldRange, source);
            });
            this.Quill.on("editor-change", (eventName, ...args) => {
                this.$emit("on-editor-change", eventName, ...args);
            });
        },
        pastHandle(evt) {
            let _this = this;
            console.log(evt)
            if (evt.clipboardData && evt.clipboardData.files && evt.clipboardData.files.length) {
                evt.preventDefault();
                [].forEach.call(evt.clipboardData.files, (file) => {
                    if (!file.type.match(/^image\/(gif|jpe?g|a?png|bmp)/i)) {
                        return;
                    }
                    let params = new FormData()
                    params.append('file', file)
                    uploadImage(params).then(res => {
                        if (res.code == 200) {
                            let length = _this.Quill.getSelection().index; //光标位置
                            // 插入图片  图片地址
                            _this.Quill.insertEmbed(length, "image", res.url);
                            // 调整光标到最后
                            _this.Quill.setSelection(length + 1); //光标后移一位
                        }
                    })
                });
            }
        },
        dropFilesHandle(evt) {
            let _this = this;
            console.log('dropFilesHandle', evt)
            if (evt.dataTransfer && evt.dataTransfer.files && evt.dataTransfer.files.length) {
                evt.preventDefault();
                [].forEach.call(evt.dataTransfer.files, (file) => {
                    if (!file.type.match(/^image\/(gif|jpe?g|a?png|bmp)/i)) {
                        return;
                    }
                    let params = new FormData()
                    params.append('file', file)
                    uploadImage(params).then(res => {
                        if (res.code == 200) {
                            let length = _this.editor.getSelection().index; //光标位置
                            // 插入图片  图片地址
                            _this.editor.insertEmbed(length, "image", res.url);
                            // 调整光标到最后
                            _this.editor.setSelection(length + 1); //光标后移一位
                        }
                    })
                });
            }
        },
        // 上传前校检格式和大小
        handleBeforeUpload(file) {
            // 校检文件大小
            if (this.fileSize) {
                const isLt = file.size / 1024 / 1024 < this.fileSize;
                if (!isLt) {
                    this.$message.error(`上传文件大小不能超过 ${this.fileSize} MB!`);
                    return false;
                }
            }
            return true;
        },
        handleUploadSuccess(res, file) {
            // 获取富文本组件实例
            let quill = this.Quill;
            // 如果上传成功
            if (res.code == 200) {
                // 获取光标所在位置
                let length = quill.getSelection().index;
                // 插入图片  res.url为服务器返回的图片地址
                quill.insertEmbed(length, "image", res.url);
                // 调整光标到最后
                quill.setSelection(length + 1);
            } else {
                this.$message.error("图片插入失败");
            }
        },
        handleUploadError() {
            this.$message.error("图片插入失败");
        },
    },
};
</script>

<style>
.editor,
.ql-toolbar {
    white-space: pre-wrap !important;
    line-height: normal !important;
}

.quill-img {
    display: none;
}

.ql-snow .ql-tooltip {
    left: 0 !important;
}

.ql-snow .ql-tooltip[data-mode="link"]::before {
    content: "请输入链接地址:";
}

.ql-snow .ql-tooltip.ql-editing a.ql-action::after {
    border-right: 0px;
    content: "保存";
    padding-right: 0px;
}
.ql-snow .ql-tooltip a.ql-action::after {
    content: "编辑";
}
.ql-snow .ql-tooltip a.ql-remove::before {
    border-right: 0px;
    content: "删除";
    color: #f56c6c;
    padding-right: 0px;
}
.ql-snow .ql-formats .ql-bold {
    position: relative;
}
.ql-snow .ql-formats .ql-bold::before {
    display: block;
    color: #333;
    position: absolute;
    bottom: -20px;
    width: 45px;
    background: rgba(0, 0, 0, 0.2);
    padding: 3px 0;
    border-radius: 3px;
    left: 50%;
    margin-left: -22px;
}

.ql-snow .ql-tooltip[data-mode="video"]::before {
    content: "请输入视频地址:";
}
.ql-snow .ql-picker.ql-size ::hover {
    content: "字体大小";
}
.ql-snow .ql-picker.ql-size .ql-picker-label::before,
.ql-snow .ql-picker.ql-size .ql-picker-item::before {
    content: "14px";
}

.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="small"]::before,
.ql-snow .ql-picker.ql-size .ql-picker-item[data-value="small"]::before {
    content: "10px";
}

.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="large"]::before,
.ql-snow .ql-picker.ql-size .ql-picker-item[data-value="large"]::before {
    content: "18px";
}

.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="huge"]::before,
.ql-snow .ql-picker.ql-size .ql-picker-item[data-value="huge"]::before {
    content: "32px";
}

.ql-snow .ql-picker.ql-header .ql-picker-label::before,
.ql-snow .ql-picker.ql-header .ql-picker-item::before {
    content: "文本";
}

.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="1"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="1"]::before {
    content: "标题1";
}

.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="2"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="2"]::before {
    content: "标题2";
}

.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="3"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="3"]::before {
    content: "标题3";
}

.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="4"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="4"]::before {
    content: "标题4";
}

.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="5"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="5"]::before {
    content: "标题5";
}

.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="6"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="6"]::before {
    content: "标题6";
}

.ql-snow .ql-picker.ql-font .ql-picker-label::before,
.ql-snow .ql-picker.ql-font .ql-picker-item::before {
    content: "标准字体";
}

.ql-snow .ql-picker.ql-font .ql-picker-label[data-value="serif"]::before,
.ql-snow .ql-picker.ql-font .ql-picker-item[data-value="serif"]::before {
    content: "衬线字体";
}

.ql-snow .ql-picker.ql-font .ql-picker-label[data-value="monospace"]::before,
.ql-snow .ql-picker.ql-font .ql-picker-item[data-value="monospace"]::before {
    content: "等宽字体";
}
</style>