<template>
  <div>
    <div>
      <div v-for="(item,index) in children" class="voidItem" :key="index">
        <!-- <span class="matrix_label">{{item.label}}</span> -->
        <el-input class="matrix_label" v-model="item.label"></el-input>
        <div v-if="type=='void'">
          <el-input :style="item.style"></el-input>
        </div>
        <div v-else-if="type=='radio'">
          <el-radio-group v-model="item.value" :style="item.style">
            <div class="radioItem">
              <div v-for="(ittem,ittIndex) in item.options" :key="ittIndex" :style="ittem.style"
                class="radioItem_radio">
                <el-radio-button v-if="item.optionType === 'button'" :label="ittem.value">{{ittem.label}}
                </el-radio-button>
                <el-radio v-else :label=ittem.value :border="item.border">{{ittem.label}}</el-radio>
              </div>
            </div>
          </el-radio-group>
        </div>
        <div v-else-if="type=='checkbox'">
          <el-checkbox-group :style="item.style" v-model="checkedList">
            <div class="radioItem">
              <div v-for="(ittem,ittIndex) in item.options" :key="ittIndex" :style="ittem.style"
                class="radioItem_radio">
                <el-checkbox-button v-if="item.optionType === 'button'" :label="ittem.value">{{ittem.label}}
                </el-checkbox-button>
                <el-checkbox v-else :label=ittem.value :border="item.border">{{ittem.label}}</el-checkbox>
              </div>
            </div>
          </el-checkbox-group>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
  export default {
    data() {
      return {
        voidList: [],
        checkedList: []
      }
    },
    props: ['children', 'type'],
    watch: {
      'children': {
        handler(newVal) {
          this.voidList = this.children
        }
      }
    },
    created() {
      this.voidList = this.children
      console.log('this.type', this.type, this.children)
    },
    methods: {}
  }
</script>

<style lang="scss" scoped>
  .radioItem {
    display: flex;
    flex-wrap: wrap;

    .radioItem_radio {
      margin-right: 10px;
    }
  }

  ::v-deep .matrix_label {
    display: block;
    margin-bottom: 5px;

    .el-input__inner {
      border-color: #fff;
      background: transparent;
    }
  }
</style>