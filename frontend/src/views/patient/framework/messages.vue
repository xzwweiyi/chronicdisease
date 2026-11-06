<template>
  <div class="container">
    <div class="btn-operate">
      <div>
        <el-button @click="goBack">返回</el-button>
        <el-button type="primary" plain @click="clearPlane">清空</el-button>
      </div>
      <el-button type="success" @click="openMobileChat">AI问答记录</el-button>
    </div>
    <el-scrollbar class="detail-scrollbar">
      <div class="earlyBox">
        <div class="warn_earlyList">
          <div
            class="warn_early_item"
            v-for="(item, index) in messageList"
            :key="index"
            :class="item.userType == '1' ? 'early_item_doctor' : ''"
          >
            <el-avatar :size="50" :src="item.headImage || userHeadImgUrl"></el-avatar>
            <div class="warn_guide_info">
              <div :class="item.userType == '1' ? 'flex_end' : 'flex_start'">
                <span class="span">{{ item.userName }}</span>
                <span class="em">{{ item.msgTime | dateFormat }}</span>
                <span class="i">{{ item.userType == "1" ? "医生" : "患者" }}</span>
              </div>
              <div class="center" v-if="item.msgType == '1'">
                {{ item.msgText }}
              </div>
              <div class="center" v-else-if="item.msgType == '2'">
                <el-image :src="item.msgText" :preview-src-list="item.imgUrlList"></el-image>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-scrollbar>
    <!-- 移动端聊天确认弹窗 -->
    <el-dialog
      :visible.sync="mobileChatVisible"
      width="400px"
      :modal="true"
      :close-on-click-modal="false"
      class="mobile-chat-confirm-dialog"
    >
      <!-- <div slot="title" class="custom-title">
        <div class="title-left">
          <i class="el-icon-chat-dot-round"></i>
          <span>AI问答记录</span>
        </div>
      </div> -->

      <div class="mobile-chat-container">
        <div class="chat-preview">
          <div class="phone-frame">
            <div class="phone-screen">
              <div class="phone-header">
                <i class="el-icon-mobile-phone"></i>
                <span>手机端聊天界面</span>
              </div>
              <div class="phone-content">
                <p>点击下方按钮将在新窗口打开手机大小的聊天页面</p>
                <div class="feature-list">
                  <div class="feature-item">
                    <i class="el-icon-check"></i>
                    <span>完整的Cookie功能</span>
                  </div>
                  <div class="feature-item">
                    <i class="el-icon-check"></i>
                    <span>独立浏览器环境</span>
                  </div>
                  <div class="feature-item">
                    <i class="el-icon-check"></i>
                    <span>最佳用户体验</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="dialog-actions">
          <el-button @click="mobileChatVisible = false">取消</el-button>
          <el-button type="primary" @click="openMobileChatWindow">在新窗口打开</el-button>
        </div>
      </div>
    </el-dialog>

    <div class="display_flex">
      <el-input placeholder="请输入内容" v-model="message" clearable type="textarea" :autosize="{ minRows: 3 }" />
      <el-button type="primary" v-if="message" @click="sendMessage">发送</el-button>
      <el-upload
        :action="uploadImgUrl"
        v-else
        :on-success="handleUploadSuccess"
        :before-upload="handleBeforeUpload"
        :on-error="handleUploadError"
        name="file"
        :headers="headers"
        :show-file-list="false"
      >
        <el-button type="primary">发送图片</el-button>
      </el-upload>
    </div>
  </div>
</template>

<script>
import { getMessagesDetail, messageSend, updateReadStatus, clearMessage } from "@/api/patient/questionnaire";
import { getToken } from "@/utils/auth";
import { encryptMessage } from "@/utils/rsa";

export default {
  data() {
    return {
      headers: {
        Authorization: "Bearer " + getToken(),
      },
      // 大小限制(MB)
      fileSize: 5,
      // 文件类型, 例如['png', 'jpg', 'jpeg']
      fileType: ["png", "jpg", "jpeg"],
      uploadImgUrl: process.env.VUE_APP_BASE_API + "/common/upload", // 上传的图片服务器地址
      userHeadImgUrl: require("@/assets/images/user_headImg.webp"),
      message: "", // 发送留言信息内容
      messageList: [], // 留言列表
      patientInfo: [],
      oldPath: "",
      personId: "",
      messageNums: "",
      mobileChatVisible: false,
      mobileChatUrl: "",
    };
  },
  created() {
    let queryInfo = this.$route.query;
    this.patientInfo = queryInfo?.row || [];
    this.oldPath = queryInfo.oldPath;
    this.personId = this.patientInfo.personId || "";
    this.messageNums = this.patientInfo.messageNums || "";

    // 获取留言数据
    this.getMessagesData();
  },

  methods: {
    goBack() {
      let oldPath = decodeURIComponent(this.oldPath);
      const obj = {
        path: oldPath,
      };
      this.$tab.closeOpenPage(obj);
    },
    // 清空
    clearPlane() {
      // 确认弹窗
      this.$confirm("确定要清空所有留言记录吗？", "确认清空", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          const params = this.messageList.map((m) => m.id);
          clearMessage(params).then((res) => {
            this.getMessagesData();
            this.$message({
              type: "success",
              message: "清空成功！",
            });
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消清空",
          });
        });
    },
    // 打开移动端聊天
    openMobileChat() {
      // 生成用户标识Cookie，确保H5页面能够识别用户
      this.generateUserCookie();

      (this.mobileChatUrl = "https://10.131.101.161:4010/#/pages/index/chat?workFlowId=5c2e3dba5d484977afb9fafd4f9d30b3&"),
        +"personName=" +
          encodeURIComponent(encryptMessage(this.patientInfo.patientName)) +
          "&mobile=" +
          encodeURIComponent(encryptMessage(this.patientInfo.patientMobile)) +
          "&personId=" +
          encodeURIComponent(encryptMessage(this.personId));

      console.log(this.mobileChatUrl);
      this.mobileChatVisible = true;
    },

    // 在新窗口打开手机大小的聊天页面
    openMobileChatWindow() {
      this.mobileChatVisible = false;

      // 计算手机屏幕的尺寸（常用的手机屏幕比例）
      const screenWidth = 375;
      const screenHeight = 667;

      // 计算窗口居中位置（修复多显示器和安全限制问题）
      const dualScreenLeft = window.screenLeft !== undefined ? window.screenLeft : window.screenX;
      const dualScreenTop = window.screenTop !== undefined ? window.screenTop : window.screenY;
      
      const viewportWidth = window.innerWidth || document.documentElement.clientWidth || screen.width;
      const viewportHeight = window.innerHeight || document.documentElement.clientHeight || screen.height;
      
      const left = Math.max(0, dualScreenLeft + (viewportWidth - screenWidth) / 2);
      const top = Math.max(0, dualScreenTop + (viewportHeight - screenHeight) / 2);

      // 打开新窗口
      const chatWindow = window.open(
        this.mobileChatUrl,
        "mobileChat",
        `width=${screenWidth},height=${screenHeight},left=${left},top=${top},menubar=no,toolbar=no,location=no,status=no,scrollbars=yes,resizable=yes`
      );

      if (!chatWindow) {
        this.$message.error("无法打开新窗口，请检查浏览器弹窗设置");
        return;
      }

      this.$message.success("已在新窗口打开手机聊天界面");
    },

    // 生成用户标识Cookie
    generateUserCookie() {
      const userData = {
        patientName: this.patientInfo.patientName,
        patientMobile: this.patientInfo.patientMobile,
        personId: this.personId,
        timestamp: Date.now(),
      };
      const userDataStr = encodeURIComponent(JSON.stringify(userData));
      document.cookie = `mobileChatUser=${userDataStr}; path=/; max-age=3600; SameSite=None; Secure`;
      console.log("已生成用户标识Cookie");
    },

    // 获取留言数据
    getMessagesData() {
      getMessagesDetail(this.personId).then((res) => {
        this.messageList = res.data.map((m) => {
          return {
            ...m,
            imgUrlList: m.msgType == "2" ? res.data.filter((f) => f.msgType == "2").map((m) => m.msgText) : [],
          };
        });

        // 添加滚动到底部逻辑
        this.$nextTick(() => {
          const scrollbar = this.$el.querySelector(".el-scrollbar__wrap") || this.$el.querySelector(".warn_earlyList");
          if (scrollbar) {
            setTimeout(() => {
              scrollbar.scrollTop = scrollbar.scrollHeight;
            }, 100);
          } else {
            console.warn("未找到滚动容器元素");
          }
        });
      });
    },
    // 上传前loading加载
    handleBeforeUpload(file) {
      let isImg = false;
      if (this.fileType.length) {
        let fileExtension = "";
        if (file.name.lastIndexOf(".") > -1) {
          fileExtension = file.name.slice(file.name.lastIndexOf(".") + 1);
        }
        isImg = this.fileType.some((type) => {
          if (file.type.indexOf(type) > -1) return true;
          if (fileExtension && fileExtension.indexOf(type) > -1) return true;
          return false;
        });
      } else {
        isImg = file.type.indexOf("image") > -1;
      }

      if (!isImg) {
        this.$modal.msgError(`文件格式不正确, 请上传${this.fileType.join("/")}图片格式文件!`);
        return false;
      }
      if (this.fileSize) {
        const isLt = file.size / 1024 / 1024 < this.fileSize;
        if (!isLt) {
          this.$modal.msgError(`上传图片大小不能超过 ${this.fileSize} MB!`);
          return false;
        }
      }
      this.$modal.loading("正在发送图片请稍候...");
      this.number++;
    },
    // 上传成功回调
    handleUploadSuccess(res) {
      let messageData = this.messageList;
      const params = {
        personId: this.personId || "",
        warnGuideConverId: messageData && messageData.length ? messageData[0].warnGuideConverId : "",
        formUserId: "",
        msgType: "2", //消息类型1文本2图片
        msgText: this.message,
        msgImageUrl: res.url,
      };
      messageSend(params).then((res) => {
        this.getMessagesData();
        this.message = "";
        this.$modal.closeLoading();
        if (this.messageNums > 0) {
          // 调用接口更新未读状态
          updateReadStatus(this.personId).then((res) => {});
        }
      });
    },
    // 上传失败
    handleUploadError() {
      this.$modal.msgError("上传图片失败，请重试");
      this.$modal.closeLoading();
    },
    // 发送留言
    sendMessage() {
      let messageData = this.messageList;
      if (!messageData) {
        this.$message.warning("留言内容不能为空");
        return;
      }
      const params = {
        personId: this.personId || "",
        warnGuideConverId: messageData && messageData.length ? messageData[0].warnGuideConverId : "",
        formUserId: "",
        msgType: "1", //消息类型1文本2图片
        msgText: this.message,
        msgImageUrl: "",
      };
      messageSend(params).then((res) => {
        this.getMessagesData();
        this.message = "";
        if (this.messageNums > 0) {
          // 调用接口更新未读状态
          updateReadStatus(this.personId).then((res) => {});
        }
      });
    },
  },
};
</script>

<style scoped lang="scss">
.container {
  height: calc(100vh - 115px);
  color: #333;
  padding: 20px;
  margin: 15px;
  background-color: #fff;
  border-radius: 5px;
  overflow: hidden;
  box-shadow: 0px 0px 10px 10px rgb(0, 0, 0, 0.01);
  display: flex;
  flex-direction: column;

  .btn-operate {
    display: flex;
    justify-content: space-between;
    margin-bottom: 20px;
  }

  .detail-scrollbar {
    height: calc(100% - 96px);
    .earlyBox {
      flex: 1;
      display: flex;
      flex-direction: column;
      padding: 20px;
      border: 1px solid #f5f5f5;
      border-radius: 5px;

      .warn_earlyList {
        flex: 1;
        overflow-y: auto;
        margin-bottom: 20px;

        .warn_early_item {
          width: 100%;
          display: flex;
          padding: 10px 0;

          .warn_guide_info {
            margin-left: 15px;
            margin-right: 0;
            max-width: calc(100% - 70px);
            // flex: 1;

            .span {
              font-size: 16px;
              font-weight: bold;
            }

            .em {
              font-size: 12px;
              color: #666;
              padding: 0 10px;
            }

            .i {
              font-size: 12px;
              padding: 5px 10px;
              color: #fff;
              background: #409eff;
              border-radius: 5px;
            }

            .center {
              display: block;
              padding: 8px 10px;
              font-size: 14px;
              margin-top: 10px;
              border: 1px solid #efefef;
              border-radius: 5px;
              word-break: break-all;
              white-space: pre-wrap;
              overflow-wrap: break-word;

              .el-image {
                width: 200px;
              }
            }
          }

          .flex_end {
            display: flex;
            flex-direction: row-reverse;
          }

          .flex_start {
            display: flex;
            flex-direction: row;
          }
        }

        .early_item_doctor {
          flex-direction: row-reverse;

          .warn_guide_info {
            margin-right: 15px;
            margin-left: 0;
          }
        }
      }
    }
  }

  .display_flex {
    display: flex;
    gap: 10px;

    .el-textarea {
      flex: 1;
    }
  }
}

// 移动端聊天确认弹窗样式
.mobile-chat-confirm-dialog {
  .custom-title {
    display: flex;
    align-items: center;
    padding: 16px 20px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    margin: 0;
    border-radius: 8px 8px 0 0;

    .title-left {
      display: flex;
      align-items: center;
      gap: 10px;

      i {
        font-size: 18px;
        color: #ffd700;
      }

      span {
        font-size: 16px;
        font-weight: 600;
      }
    }
  }

  .el-dialog__header {
    padding: 0;
    margin: 0;
  }

  .el-dialog__body {
    padding: 0;
  }

  .mobile-chat-container {
    padding: 24px;
    background: #f8f9fa;
    border-radius: 0 0 8px 8px;

    .chat-preview {
      margin-bottom: 24px;

      .phone-frame {
        display: flex;
        justify-content: center;

        .phone-screen {
          width: 280px;
          height: 400px;
          background: #333;
          border-radius: 20px;
          padding: 8px;
          box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
          position: relative;

          .phone-header {
            height: 40px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border-radius: 10px 10px 0 0;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-size: 14px;
            gap: 8px;

            i {
              font-size: 16px;
              color: #ffd700;
            }
          }

          .phone-content {
            height: calc(100% - 40px);
            background: white;
            border-radius: 0 0 10px 10px;
            padding: 16px;
            display: flex;
            flex-direction: column;
            justify-content: center;

            p {
              margin: 0 0 16px 0;
              color: #666;
              font-size: 14px;
              text-align: center;
            }

            .feature-list {
              .feature-item {
                display: flex;
                align-items: center;
                gap: 8px;
                margin-bottom: 8px;
                font-size: 13px;
                color: #333;

                i {
                  color: #67c23a;
                  font-size: 14px;
                }
              }
            }
          }
        }
      }
    }

    .dialog-actions {
      display: flex;
      justify-content: center;
      gap: 16px;
    }
  }

  .el-dialog {
    border-radius: 8px;
    overflow: hidden;
    box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
    background: white;
  }
}
</style>
