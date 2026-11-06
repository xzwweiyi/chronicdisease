<template>
  <div class="contact-float-panel">
    <el-tooltip :disabled="hasPhone" content="患者无手机号" placement="left">
      <div class="contact-btn" :class="{ disabled: !hasPhone }" @click="onCall">
        <i class="el-icon-phone-outline"></i>
        <span>拨打电话</span>
      </div>
    </el-tooltip>
    <el-tooltip :disabled="hasPhone" content="患者无手机号" placement="left">
      <div class="contact-btn" :class="{ disabled: !hasPhone }" @click="onSms">
        <i class="el-icon-chat-dot-round"></i>
        <span>发送短信</span>
      </div>
    </el-tooltip>
    <el-tooltip :disabled="hasWechat" content="患者未绑定微信" placement="left">
      <div class="contact-btn" :class="{ disabled: !hasWechat }" @click="onWechat">
        <i class="el-icon-chat-line-round"></i>
        <span>发送微信</span>
      </div>
    </el-tooltip>
  </div>
</template>

<script>
import { callPatient, sendSms, sendWechat } from "@/api/follow/work";

export default {
  name: "ContactFloatPanel",
  props: {
    patientId: {
      type: [String, Number],
      default: "",
    },
    // 患者手机号（来自详情接口）
    phone: {
      type: String,
      default: "",
    },
    // 患者姓名
    patientName: {
      type: String,
      default: "",
    },
    // 是否已绑定微信
    hasWechat: {
      type: Boolean,
      default: false,
    },
  },
  computed: {
    hasPhone() {
      return !!this.phone;
    },
  },
  methods: {
    onCall() {
      if (!this.hasPhone) {
        this.$message.warning("患者无手机号");
        return;
      }
      this.$confirm(`确定拨打患者电话 ${this.phone} 吗？`, "提示", {
        confirmButtonText: "拨打",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => callPatient(this.patientId))
        .then(() => {
          this.$message.success("已触发拨号");
        })
        .catch((err) => {
          if (err === "cancel" || err === "close") return;
        });
    },
    onSms() {
      if (!this.hasPhone) {
        this.$message.warning("患者无手机号");
        return;
      }
      this.$confirm(`确定向患者 ${this.patientName || ""} 发送短信吗？`, "提示", {
        confirmButtonText: "发送",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => sendSms(this.patientId))
        .then(() => {
          this.$message.success("短信已发送");
        })
        .catch((err) => {
          if (err === "cancel" || err === "close") return;
        });
    },
    onWechat() {
      if (!this.hasWechat) {
        this.$message.warning("患者未绑定微信");
        return;
      }
      this.$prompt("请输入提醒内容", "发送微信", {
        confirmButtonText: "发送",
        cancelButtonText: "取消",
        inputValue: "请及时完成随访",
        inputPlaceholder: "请输入提醒内容",
        inputValidator: (value) => (value && value.trim() ? true : "提醒内容不能为空"),
      })
        .then(({ value }) => sendWechat(this.patientId, value))
        .then(() => {
          this.$message.success("微信提醒已发送");
        })
        .catch((err) => {
          if (err === "cancel" || err === "close") return;
        });
    },
  },
};
</script>

<style lang="scss" scoped>
.contact-float-panel {
  position: fixed;
  right: 24px;
  top: 50%;
  transform: translateY(-50%);
  z-index: 2000;
  display: flex;
  flex-direction: column;
  gap: 12px;

  .contact-btn {
    width: 64px;
    padding: 12px 0;
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 6px;
    background: #409eff;
    border: 1px solid #409eff;
    border-radius: 8px;
    box-shadow: 0 2px 12px rgba(64, 158, 255, 0.35);
    color: #fff;
    font-size: 12px;
    cursor: pointer;
    transition: all 0.2s;

    i {
      font-size: 22px;
    }

    &:hover {
      background: #66b1ff;
      border-color: #66b1ff;
    }

    &:active {
      background: #3a8ee6;
      border-color: #3a8ee6;
    }

    &.disabled {
      background: #f5f5f5;
      color: #c0c4cc;
      border-color: #e5e7eb;
      box-shadow: none;
      cursor: not-allowed;

      &:hover {
        background: #f5f5f5;
        color: #c0c4cc;
        border-color: #e5e7eb;
      }
    }
  }
}
</style>
