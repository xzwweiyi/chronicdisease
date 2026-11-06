<template>
  <div class="right-board">
    <el-tabs v-model="currentTab" class="right-panel-tabs">
      <el-tab-pane label="组件属性" name="field" />
      <el-tab-pane label="项目查看" name="project" v-if="activeData.formType == '2' && formType != 2" />
      <el-tab-pane
        v-if="
          formType == '3' && (activeData.labelType == 'Checkbox' || activeData.labelType == 'Radio' || activeData.labelType == 'Select')
        "
        label="设置评分规则"
        name="itemScores"
      />
      <el-tab-pane label="设置预警规则" name="buildWarning" v-if="isShowEarlyWarn" />
    </el-tabs>
    <div class="field-box">
      <a class="document-link" v-show="currentTab === 'field'" target="_blank" :href="documentLink" title="查看组件文档">
        <i class="el-icon-link" />
      </a>
      <el-scrollbar class="right-scrollbar">
        <!-- 组件属性 -->
        <el-form v-show="currentTab === 'field' && showField" label-width="150px" class="right_property_form" @submit.native.prevent inline>
          <!--======================= 表单设置 ========================-->
          <!-- <el-form-item label="表单编码" v-if="activeData.formCode != undefined&&isShowPropertyCode!=false">
                        <el-input v-model="activeData.formCode" placeholder="请输入表单编码" @change="formCodeChange" class="property_input" />
                    </el-form-item> -->
          <el-form-item label="表单名称" v-if="activeData.formCode != undefined">
            <el-input v-model="activeData.formName" placeholder="请输入表单名称" class="property_input" />
          </el-form-item>
          <!--======================= 表单设置 ========================-->
          <!-- <el-form-item v-if="activeData.propertyCode!==undefined&&isShowPropertyCode!=false" label="字段code">
                        <el-input class="property_input" v-model="activeData.propertyCode" placeholder="请输入字段Code" />
                    </el-form-item> -->
          <el-form-item v-if="activeData.placeholder !== undefined" label="占位提示">
            <el-input class="property_input" v-model="activeData.placeholder" placeholder="请输入占位提示" />
          </el-form-item>
          <el-form-item v-if="activeData.required !== undefined" label="是否必填">
            <el-switch v-model="activeData.required" />
          </el-form-item>
          <el-form-item v-if="activeData.formCode || activeData.propertyCode" label="是否有副标题">
            <el-switch v-model="activeData.isShowSubTitle" />
          </el-form-item>
          <el-form-item v-if="activeData.labelType == 'Input' || activeData.labelType == 'InputNumber'" label="是否有单位">
            <el-switch v-model="activeData.isShowUnit" />
            <el-input class="unit_input" clearable v-if="activeData.isShowUnit" v-model="activeData.unit"></el-input>
          </el-form-item>
          <el-form-item v-if="activeData.labelType == 'TimeNumeral'" label="后缀名称">
            <el-input class="unit_input" clearable v-model="activeData.unit"></el-input>
          </el-form-item>
          <el-form-item v-if="activeData.labelType" label="题目展示">
            <el-slider v-model="activeData.span" :max="24" :min="4"></el-slider>
          </el-form-item>
          <el-form-item
            label="是否动态添加"
            v-if="activeData.openMultiple !== undefined && activeData.formType !== '2' && activeData.formType != 1"
          >
            <el-switch active-value="1" inactive-value="0" v-model="activeData.openMultiple" />
          </el-form-item>
          <el-form-item label="是否显示在医生端" v-if="activeData.propertyCode || activeData.formCode">
            <el-switch :active-value="true" :inactive-value="false" v-model="activeData.isDoctorShow" />
          </el-form-item>
          <el-form-item label="是否显示在患者端" v-if="activeData.propertyCode || activeData.formCode">
            <el-switch :active-value="true" :inactive-value="false" v-model="activeData.isPatientShow" />
          </el-form-item>
          <el-form-item v-if="activeData.subjectScores != undefined" label="题目分数">
            <el-input-number
              :controls="false"
              @change="subjectScoresChange"
              v-model="activeData.subjectScores"
              :min="0"
              :max="100"
              placeholder="请输入题目分数"
            />
          </el-form-item>
          <el-form-item v-if="activeData['start-placeholder'] !== undefined" label="开始占位">
            <el-input v-model="activeData['start-placeholder']" placeholder="请输入占位提示" />
          </el-form-item>
          <el-form-item v-if="activeData['end-placeholder'] !== undefined" label="结束占位">
            <el-input v-model="activeData['end-placeholder']" placeholder="请输入占位提示" />
          </el-form-item>
          <el-form-item v-if="activeData.labelType === 'Input' && !regList.maxlength" label="内容限制">
            <el-select v-model="activeData.regValue" @change="regChange" placeholder="请选择内容限制" class="property_input">
              <el-option v-for="item in regList" :key="item.value" :label="item.label" :value="item"> </el-option>
            </el-select>
          </el-form-item>
          <el-form-item
            v-if="
              activeData.tag === 'el-input' &&
              activeData.regValue &&
              activeData.regValue.value != 'IDNumber' &&
              activeData.regValue.value != 'mobile' &&
              activeData.regValue.value != 'email' &&
              activeData.regValue.value != 'number'
            "
            label="最大位数"
          >
            <el-input type="number" v-model="activeData.maxlength" placeholder="请输入内容最长字符数" class="property_input" />
          </el-form-item>

          <el-form-item
            v-if="isShowMin || activeData.tag === 'el-checkbox-group'"
            :label="activeData.tag === 'el-checkbox-group' ? '可选项最小值' : '最小值'"
          >
            <el-input-number :controls="false" v-model="activeData.min" placeholder="最小值" />
          </el-form-item>
          <el-form-item
            v-if="isShowMax || activeData.tag === 'el-checkbox-group'"
            :label="activeData.tag === 'el-checkbox-group' ? '可选项最大值' : '最大值'"
          >
            <el-input-number :controls="false" :min="1" v-model="activeData.max" placeholder="最大值" />
          </el-form-item>
          <el-form-item v-if="activeData.tag === 'el-input-number'" label="步数">
            <el-input-number :controls="false" :min="0" v-model="activeData.step" placeholder="步数值" />
          </el-form-item>
          <el-form-item v-if="activeData.tag === 'el-input-number'" label="是否显示控制按钮">
            <el-switch :active-value="true" :inactive-value="false" v-model="activeData.controls" />
          </el-form-item>
          <el-form-item v-if="activeData.tag === 'el-input-number'" label="小数点位数">
            <el-input-number :controls="false" :min="0" :max="10" v-model="activeData.precision" placeholder="精度值" />
          </el-form-item>
          <el-form-item
            v-if="
              activeData.type !== undefined &&
              ('el-date-picker' === activeData.tag || activeData.labelType == 'YEARMONTHDAY' || 'com-TimeNumeral' === activeData.tag)
            "
            label="时间类型"
          >
            <el-select v-model="activeData.type" placeholder="请选择时间类型" class="property_input" @change="dateTypeChange">
              <el-option v-for="(item, index) in dateOptions" :key="index" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item v-if="activeData.format !== undefined" label="时间格式">
            <el-input
              :value="activeData.format"
              class="property_input"
              disabled
              placeholder="请输入时间格式"
              @input="setTimeValue($event)"
            />
          </el-form-item>
          <el-form-item v-if="activeData.labelType == 'Date' && activeData.pickerOptions" label="最大值">
            <el-date-picker
              v-model="activeData.pickerOptions.maxDate"
              :type="activeData.type"
              :format="activeData.format"
              :value-format="activeData.format"
              placeholder="选择日期"
              :picker-options="maxPickerOption"
              @change="maxDateChange"
            />
          </el-form-item>
          <el-form-item v-if="activeData.labelType == 'Date' && activeData.pickerOptions" label="最小值">
            <el-date-picker
              v-model="activeData.pickerOptions.minDate"
              :type="activeData.type"
              :format="activeData.format"
              :value-format="activeData.format"
              placeholder="选择日期"
              :picker-options="minPickerOption"
              @change="minDateChange"
            />
          </el-form-item>
          <el-form-item v-if="activeData.separator !== undefined" label="分隔文字">
            <el-input class="unit_input" clearable v-model="activeData.separator"></el-input>
          </el-form-item>
          <el-form-item v-if="activeData.yearUnit !== undefined" label="是否有年">
            <el-switch v-model="activeData.isShowYear" />
            <el-input class="unit_input" clearable v-model="activeData.yearUnit"></el-input>
          </el-form-item>
          <el-form-item v-if="activeData.monthUnit !== undefined" label="是否有月">
            <el-switch v-model="activeData.isShowMonth" />
            <el-input class="unit_input" clearable v-model="activeData.monthUnit"></el-input>
          </el-form-item>
          <el-form-item v-if="activeData.dayUnit !== undefined" label="是否有天">
            <el-switch v-model="activeData.isShowDay" />
            <el-input class="unit_input" clearable v-model="activeData.dayUnit"></el-input>
          </el-form-item>
          <el-form-item v-if="activeData.startDateLabel !== undefined" label="开始时间文字">
            <el-input class="unit_input" clearable v-model="activeData.startDateLabel"></el-input>
          </el-form-item>
          <el-form-item v-if="activeData.endDateLabel !== undefined" label="结束时间文字">
            <el-input class="unit_input" clearable v-model="activeData.endDateLabel"></el-input>
          </el-form-item>
          <el-form-item v-if="activeData.differenceLabel !== undefined" label="计算结果文字">
            <el-input class="unit_input" clearable v-model="activeData.differenceLabel"></el-input>
          </el-form-item>
          <el-form-item v-if="activeData.differenceUnit !== undefined" label="计算结果单位">
            <el-input class="unit_input" clearable v-model="activeData.differenceUnit"></el-input>
          </el-form-item>
          <el-form-item v-if="activeData.isSetEndTime !== undefined" label="是否设置结束时间">
            <el-switch v-model="activeData.isSetEndTime" />
          </el-form-item>
          <!-- <el-form-item label="设置日期范围" class="dateRange_form_item"
                        v-if="(activeData.labelType=='Date'&&activeData.pickerOptions)">
                        <div class="dateranger_set_tip">天数设置为正数表示“所选日期后n天”，
                            负数表示“所选日期前n天”，0表示“所选日期当天”</div>
                        <div class="dateRange_form_itemInfo">
                            <span>起始日期：</span>
                            <el-select v-model="activeData.startType">
                                <el-option label="不限制" value="1"></el-option>
                                <el-option label="填写当日" value="2"></el-option>
                                <el-option label="指定日期" value="3"></el-option>
                            </el-select>
                            <el-date-picker v-if="activeData.startType==3"
                             v-model="activeData.pickerOptions.minDate" :type="activeData.type" 
                            :format="activeData.format" :value-format="activeData.valueFormat" placeholder="选择日期" 
                            :picker-options="maxPickerOption" @change="maxDateChange" />
                            <el-input-number :controls="false" v-if="activeData.startType==2" 
                            v-model="maxDayNum"></el-input-number>
                        </div>
                        <div class="dateRange_form_itemInfo">
                            <span>截止日期：</span>
                            <el-select v-model="activeData.endType" @change="endTypeChange" class="dateranger_type">
                                <el-option v-for="(item,index) in endDaterangeTypeList" :key="index"
                                :label="item.label" :value="item.value+''"></el-option>
                            </el-select>
                            <el-date-picker v-if="activeData.endType==3"
                            v-model="activeData.pickerOptions.maxDate" :type="activeData.type" 
                            :format="activeData.format" :value-format="activeData.valueFormat" 
                            placeholder="选择日期" :picker-options="minPickerOption" @change="minDateChange" />
                            <el-input-number :controls="false" v-if="activeData.endType==2"
                            v-model="minDayNum"></el-input-number>
                        </div>
                    </el-form-item> -->
          <el-form-item v-if="activeData.tag == 'el-divider'" label="分割线样式">
            <el-select v-model="activeData.lineType" class="property_input" @change="directionChange">
              <el-option label="实线" value="solid" />
              <el-option label="虚线" value="dashed" />
              <el-option label="点线" value="dotted" />
            </el-select>
          </el-form-item>
          <el-form-item v-if="activeData.name !== undefined && activeData.tag == 'el-upload'" label="文件字段名">
            <el-input v-model="activeData.name" placeholder="请输入上传文件字段名" class="property_input" />
          </el-form-item>
          <el-form-item v-if="activeData.accept !== undefined" label="文件类型">
            <el-select v-model="activeData.accept" placeholder="请选择文件类型" class="property_input" clearable>
              <el-option label="图片" value="image/*" />
              <el-option label="视频" value="video/*" />
              <el-option label="音频" value="audio/*" />
              <el-option label="excel" value=".xls,.xlsx" />
              <el-option label="word" value=".doc,.docx" />
              <el-option label="pdf" value=".pdf" />
              <el-option label="txt" value=".txt" />
            </el-select>
          </el-form-item>
          <el-form-item v-if="activeData.fileSize !== undefined" label="文件大小">
            <el-input v-model.number="activeData.fileSize" placeholder="请输入文件大小" class="property_input">
              <el-select slot="append" v-model="activeData.sizeUnit" :style="{ width: '66px' }">
                <el-option label="KB" value="KB" />
                <el-option label="MB" value="MB" />
                <el-option label="GB" value="GB" />
              </el-select>
            </el-input>
          </el-form-item>
          <el-form-item v-if="activeData['range-separator'] !== undefined" label="分隔符">
            <el-input v-model="activeData['range-separator']" class="property_input" placeholder="请输入分隔符" />
          </el-form-item>
          <el-form-item v-if="activeData.labelType == 'timePicker'" label="时间段">
            <el-input v-model="activeData['picker-options'].selectableRange" placeholder="请输入时间段" class="property_input" />
          </el-form-item>

          <el-form-item v-if="activeData['allow-half'] !== undefined" label="允许半选">
            <el-switch v-model="activeData['allow-half']" @change="allowHalfChange" />
          </el-form-item>
          <el-form-item v-if="activeData['show-score'] !== undefined" label="显示分数">
            <el-switch v-model="activeData['show-score']" @change="rateScoreChange" />
          </el-form-item>
          <!-- <el-form-item v-if="activeData.tag === 'el-cascader'" label="是否多选">
                        <el-switch v-model="activeData.props.props.multiple" />
                    </el-form-item> -->
          <el-form-item v-if="activeData.showTip !== undefined" label="显示提示">
            <el-switch v-model="activeData.showTip" />
          </el-form-item>
          <el-form-item v-if="activeData.tag === 'el-select'" label="是否可搜索">
            <el-switch v-model="activeData.filterable" />
          </el-form-item>
          <!-- <el-form-item v-if="activeData.required !== undefined" label="计算公式">
                        <el-link type="primary" @click="handleFormula">公式设置</el-link>
                    </el-form-item> -->
          <el-form-item v-if="activeData.tag == 'el-statistic'" label="计算公式">
            <el-link type="primary" @click="handleFormula">公式设置</el-link>
          </el-form-item>
          <el-form-item v-if="activeData.isFlex !== undefined && activeData.tagType != 'tool'" label="是否换行">
            <el-switch v-model="activeData.isFlex" />
          </el-form-item>
          <el-form-item v-if="activeData.propertyCode || activeData.formCode" label="是否有注释说明">
            <el-switch v-model="activeData.isExplanatoryNote" />
          </el-form-item>
          <!-- <editor class="custom_editor explanatoryNote_editor" v-if="activeData.isExplanatoryNote" 
                        v-model="activeData.explanatoryNote" :toolbar="toolbar" :min-height="192" /> -->
          <VueQuillEditor v-show="activeData.isExplanatoryNote" ref="QuillEditor" v-model="activeData.explanatoryNote" />

          <el-form-item v-if="activeData['icon'] !== undefined && activeData.tag === 'el-button'" label="按钮图标">
            <el-input v-model="activeData['icon']" placeholder="请输入按钮图标名称">
              <el-button slot="append" icon="el-icon-thumb" @click="openIconsDialog('icon')"> 选择 </el-button>
            </el-input>
          </el-form-item>
          <el-form-item v-if="activeData.type !== undefined && activeData.tag === 'el-button'" label="按钮类型">
            <el-select v-model="activeData.type" :style="{ width: '100%' }">
              <el-option label="primary" value="primary" />
              <el-option label="success" value="success" />
              <el-option label="warning" value="warning" />
              <el-option label="danger" value="danger" />
              <el-option label="info" value="inf1o" />
              <el-option label="text" value="text" />
            </el-select>
          </el-form-item>
          <el-form-item
            v-if="activeData.description !== undefined && activeData.tag == 'com-caption'"
            label="说明文字内容"
            style="display: flex; flex-direction: column"
          >
            <!-- <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 4}" v-model="activeData.description" 
                            placeholder="请输入说明文字内容" clearable /> -->
            <VueQuillEditor ref="vueQuillEditorHtml" v-model="activeData.description" />
          </el-form-item>
          <el-form-item v-if="activeData.type !== undefined && activeData.tag == 'com-caption'" label="颜色主题选择">
            <el-select v-model="activeData.type" clearable>
              <el-option v-for="(f, i) in captionThemeType" :key="i" :label="f.value" :value="f.value"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item v-if="activeData.effect !== undefined && activeData.tag == 'com-caption'" label="亮度主题选择">
            <el-select v-model="activeData.effect" :disabled="!activeData.type">
              <el-option v-for="(f, i) in captionEffect" :key="i" :label="f.value" :value="f.value"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="最多个数" v-if="activeData.limit != undefined">
            <el-input v-model="activeData.limit" placeholder="请输入文件最大数量" class="property_input" />
          </el-form-item>
          <el-form-item v-if="activeData.buttonText !== undefined" v-show="'picture-card' !== activeData['list-type']" label="按钮文字">
            <el-input v-model="activeData.buttonText" placeholder="请输入按钮文字" class="property_input" />
          </el-form-item>
          <el-form-item v-if="activeData.tag === 'el-button'" label="按钮文字">
            <el-input v-model="activeData.default" placeholder="请输入按钮文字" class="property_input" />
          </el-form-item>
          <el-form-item v-if="activeData['active-color'] !== undefined" label="开启颜色">
            <el-color-picker v-model="activeData['active-color']" @change="activeColorChange" />
          </el-form-item>
          <el-form-item v-if="activeData['inactive-color'] !== undefined" label="关闭颜色">
            <el-color-picker v-model="activeData['inactive-color']" @change="inactiveColorChange" />
          </el-form-item>
        </el-form>
        <div v-show="currentTab === 'project' && showField">
          <div v-if="libraryProperties.length">
            <div v-for="item in libraryProperties" :key="item.formCode">
              <div v-if="item.propertyCode != 'openImage'" class="properties_item">
                <div class="properties_info">
                  <p class="properties_title">项目名称</p>
                  <p>{{ item.propertyName }}</p>
                </div>
                <div class="properties_info">
                  <p class="properties_title">填写方式</p>
                  <p v-if="item.labelType != 'form'">
                    {{ item.propertyType | convertTagTypeStr }}
                  </p>
                  <p v-else>问题组</p>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div v-show="currentTab === 'itemScores' && showField">
          <div class="article_fraction">
            <p class="article_fraction_tit">项目分值</p>
            <div v-for="(item, index) in activeData.options" :key="index" class="article_fraction_item">
              <div class="article_item_text">{{ item.label }}</div>
              <div class="article_item_fraction">
                <span class="">分数</span>
                <el-input-number :controls="false" v-model="item.itemScores" class="fraction_number" :step="1"></el-input-number>
              </div>
            </div>
          </div>
        </div>
        <div v-show="currentTab === 'buildWarning' && showField">
          <div class="article_fraction" v-if="warnMedicalEarlyInfo && warnMedicalEarlyInfo.length">
            <p class="article_fraction_tit">
              预警规则
              <span class="article_fraction_tip">
                {{
                  isShowEarlyWarn &&
                  (activeData.labelType == "Radio" || activeData.labelType == "Checkbox" || activeData.labelType == "Select")
                    ? "(勾选对应的选项，即进行预警)"
                    : "(设置对应的预警区间)"
                }}
              </span>
            </p>
            <el-radio-group v-model="warningMedicalIsCondition" v-if="activeData.labelType == 'Input'">
              <el-radio label="1">分类预警</el-radio>
              <el-radio label="2">无分类预警</el-radio>
            </el-radio-group>
            <p class="article_fraction_tit">预警提示</p>
            <el-input
              v-model="warnPreDesc"
              placeholder="请输入预警提示"
              style="padding: 15px 0"
              type="textarea"
              :autosize="{ minRows: 3 }"
            ></el-input>
            <el-button class="addWarning_btn" type="primary" @click="addWarningList">增加预警 </el-button>
            <el-scrollbar class="warnMedical_earlyList" ref="warnEarlyScrollbar">
              <div v-for="(item, index) in warnEarlyConditionForms" class="earlyWarn_content" :key="index">
                <p class="earlyWarn_tit">请选择预警等级</p>
                <el-select v-model="item.earlyWarnLevel" style="width: 100%; margin: 10px 0">
                  <el-option label="一级" value="一级"></el-option>
                  <el-option label="二级" value="二级"></el-option>
                  <el-option label="三级" value="三级"></el-option>
                  <el-option label="轻度" value="轻度"></el-option>
                  <el-option label="中度" value="中度"></el-option>
                  <el-option label="高度" value="高度"></el-option>
                  <el-option label="弱阳性" value="弱阳性"></el-option>
                  <el-option label="阳性" value="阳性"></el-option>
                  <el-option label="异常" value="异常"></el-option>
                  <el-option label="危急" value="危急"></el-option>
                </el-select>
                <!-- <el-radio-group v-model="item.earlyWarnLevel">
                                    <el-radio label="1">异常</el-radio>
                                    <el-radio label="2">危急</el-radio>
                                </el-radio-group> -->
                <div v-if="activeData.labelType == 'Input' || activeData.labelType == 'InputNumber'">
                  <div v-if="warningMedicalIsCondition == 1">
                    <p class="earlyWarn_tit">分类条件</p>
                    <el-form>
                      <el-form-item label="性别">
                        选择
                        <el-select class="condition_select" v-model="item.sexCondition">
                          <el-option label="男" value="1"></el-option>
                          <el-option label="女" value="2"></el-option>
                        </el-select>
                        时
                      </el-form-item>
                      <el-form-item label="年龄">
                        该题填写
                        <el-select class="condition_select" v-model="item.ageConditionType">
                          <el-option
                            v-for="(condItem, condIndex) in conditionOption"
                            :key="condIndex"
                            :label="condItem.label"
                            :value="condItem.value"
                          ></el-option>
                        </el-select>
                        <div class="earlyWarn_input">
                          <el-input-number
                            :controls="false"
                            class="earlyWarn_number"
                            :min="0"
                            v-if="item.ageConditionType == '6'"
                            v-model="item.ageEarlyWarnStart"
                          />
                          <el-input-number
                            :controls="false"
                            class="earlyWarn_number"
                            :min="0"
                            v-if="item.ageConditionType == '6'"
                            v-model="item.ageEarlyWarnEnd"
                          />
                          <el-input-number
                            :controls="false"
                            class="earlyWarn_number"
                            :min="0"
                            v-if="item.ageConditionType != '6'"
                            v-model="item.ageEarlyWarnValue"
                          />
                          时
                        </div>
                      </el-form-item>
                    </el-form>
                  </div>
                  <p class="earlyWarn_tit">范围值</p>
                  <el-select v-model="item.conditionType" class="earlyWarn_type">
                    <el-option
                      v-for="(symbolItem, symbolIndex) in warningSymbol"
                      :key="symbolIndex"
                      :label="symbolItem.label"
                      :value="symbolItem.value"
                    ></el-option>
                  </el-select>
                  <div class="earlyWarn_input">
                    <el-input-number
                      :controls="false"
                      class="earlyWarn_number"
                      :min="0"
                      v-if="item.conditionType == '6'"
                      v-model="item.earlyWarnStart"
                    />
                    <el-input-number
                      :controls="false"
                      class="earlyWarn_number"
                      :min="0"
                      v-if="item.conditionType == '6'"
                      v-model="item.earlyWarnEnd"
                    />
                    <el-input-number
                      :controls="false"
                      class="earlyWarn_number"
                      :min="0"
                      v-if="item.conditionType != '6'"
                      v-model="item.earlyWarnValue"
                    />
                  </div>
                </div>
                <div v-else-if="activeData.labelType == 'Radio' || activeData.labelType == 'Checkbox' || activeData.labelType == 'Select'">
                  <p class="earlyWarn_tit">请勾选预警项</p>
                  <el-checkbox-group
                    v-model="item.earlyWarnValue"
                    @change="earlyWarnChange"
                    v-if="activeData.options && activeData.options.length"
                  >
                    <el-checkbox
                      v-for="(earlyWarnItem, earlyWarnIndex) in activeData.options"
                      :key="earlyWarnIndex"
                      :label="earlyWarnItem.value.toString()"
                    >
                      {{ earlyWarnItem.label }}</el-checkbox
                    >
                  </el-checkbox-group>
                </div>
                <el-button
                  type="danger"
                  plain
                  class="warning_deleteBtn"
                  v-if="warnEarlyConditionForms.length > 1"
                  @click="deleteWaringInfo(index)"
                  >删除
                </el-button>
              </div>
            </el-scrollbar>
          </div>
        </div>
      </el-scrollbar>
    </div>

    <treeNode-dialog :visible.sync="dialogVisible" title="添加选项" @commit="addNode" />
    <icons-dialog :visible.sync="iconsVisible" :current="activeData[currentIconModel]" @select="setIcon" />
  </div>
</template>

<script>
import draggable from "vuedraggable";
import TreeNodeDialog from "./TreeNodeDialog";
import { isNumberStr } from "@/utils/index";
import IconsDialog from "./IconsDialog";

import { inputComponents, selectComponents } from "@/utils/generator/config";
import { deleteMedicalCondition } from "@/api/crf/fllowupCRF";

import {
  dateTimeFormat,
  daterangeTypes,
  warningInfo,
  dateTypeOptions,
  dateRangeTypeOptions,
  colorFormatOptions,
  justifyOptions,
  regListOptions,
  warningSymbolOptions,
  conditionOptions,
  toolbarData,
} from "./rightPanel_data";

import VueQuillEditor from "@/components/vueQuillEditor/index"; // 富文本编辑器
// import QuillEditor from '@/components/QuillEditor/index' // 富文本编辑器

export default {
  components: {
    draggable,
    TreeNodeDialog,
    IconsDialog,
    VueQuillEditor,
  },
  props: ["showField", "activeData", "formConf", "formulaCurrent", "formType", "isShowPropertyCode"],
  data() {
    return {
      toolbar: toolbarData,
      earlyWarnOption: [],
      currentTab: "field",
      currentNode: null,
      dialogVisible: false,
      iconsVisible: false,
      currentIconModel: null,

      dateTypeOptions: dateTypeOptions(),
      dateRangeTypeOptions: dateRangeTypeOptions(),
      colorFormatOptions: colorFormatOptions(),
      justifyOptions: justifyOptions(),
      layoutTreeProps: {
        label(data, node) {
          return data.formCode || `${data.label}: ${data.vModel}`;
        },
      },
      regList: regListOptions(),
      maxPickerOption: {
        disabledDate: (time) => {
          if (this.activeData.pickerOptions && this.activeData.pickerOptions.minDate !== null) {
            let minDateVal = new Date(this.activeData.pickerOptions.minDate).getTime();
            // 只能选 minDate 前后一年的范围
            return new Date(time).valueOf() <= minDateVal;
          }
          return false;
        },
      },
      minPickerOption: {
        disabledDate: (time) => {
          if (this.activeData.pickerOptions && this.activeData.pickerOptions.maxDate !== null) {
            let maxDateVal = new Date(this.activeData.pickerOptions.maxDate).getTime();
            // 只能选 maxDate 前后一年的范围
            return new Date(time).valueOf() >= maxDateVal;
          }
          return false;
        },
      },
      maxDayNum: 0,
      minDayNum: 0,
      endDaterangeTypeList: [daterangeTypes[0]],
      warningSymbol: warningSymbolOptions(),
      conditionOption: conditionOptions(),
      warnEarlyConditionForms: [],
      warningMedicalIsCondition: "",
      warnPreDesc: "", //预警提示
      captionThemeType: [{ value: "success" }, { value: "warning" }, { value: "info" }, { value: "error" }],
      captionEffect: [{ value: "light" }, { value: "dark" }],
    };
  },
  computed: {
    documentLink() {
      return this.activeData.document || "https://element.eleme.cn/#/zh-CN/component/installation";
    },
    dateOptions() {
      if (this.activeData.type !== undefined) {
        if (this.activeData.tag === "el-date-picker" || this.activeData.labelType === "YEARMONTHDAY") {
          if (this.activeData["start-placeholder"] === undefined) {
            return this.dateTypeOptions;
          }
          return this.dateRangeTypeOptions;
        } else if (this.activeData.tag === "com-TimeNumeral") {
          let nonTimeTypeOptions = this.dateTypeOptions.filter((f) => f.value != "datetime" && f.value != "week");
          return nonTimeTypeOptions;
        }
      }
      return [];
    },
    activeTag() {
      return this.activeData.tag;
    },
    isShowMin() {
      if (this.activeData.tag == "el-input" && this.activeData.regValue && this.activeData.regValue.value == "number") {
        return true;
      } else {
        return ["el-input-number", "el-slider"].indexOf(this.activeTag) > -1;
      }
    },
    isShowMax() {
      if (this.activeData.tag == "el-input" && this.activeData.regValue && this.activeData.regValue.value == "number") {
        return true;
      } else {
        //  'el-rate' 评分固定为5颗星
        return ["el-input-number", "el-slider"].indexOf(this.activeTag) > -1;
      }
    },
    isShowStep() {
      if (this.activeData.tag == "el-input" && this.activeData.regValue && this.activeData.regValue.value == "number") {
        return true;
      } else {
        return ["el-input-number", "el-slider"].indexOf(this.activeTag) > -1;
      }
    },
    libraryProperties() {
      const { formType, libraryForms, libraryProperties, medicalProperties, templateProperties, medicalForms } = this.activeData;
      if (formType && formType == "2") {
        let children = [];
        if (libraryProperties && libraryProperties.length) {
          children = children.concat(libraryProperties);
        }
        if (templateProperties && templateProperties.length) {
          children = children.concat(templateProperties);
        }
        if (libraryForms && libraryForms.length) {
          children = children.concat(libraryForms);
        }
        if (medicalProperties && medicalProperties.length) {
          children = children.concat(medicalProperties);
        }
        if (medicalForms && medicalForms.length) {
          children = children.concat(medicalForms);
        }
        if (children.length) {
          children = children.map((item) => {
            return {
              ...JSON.parse(item.attribute),
              ...item,
            };
          });
          children.sort((a, b) => {
            return a.sortNum - b.sortNum;
          });
        }
        return children;
      } else {
        return [];
      }
    },
    isShowEarlyWarn() {
      let isShowTab =
        this.formType == "4" &&
        (this.activeData.labelType == "Radio" ||
          this.activeData.labelType == "Select" ||
          this.activeData.labelType == "Checkbox" ||
          (this.activeData.labelType == "Input" && this.activeData.regValue && this.activeData.regValue.value == "number"));
      return isShowTab;
    },
    warnMedicalEarlyInfo() {
      let params = this.$route.params;
      let warnMedicalEarlyInfo = [];
      let currentLabelType = this.activeData.labelType;
      if (
        ((currentLabelType == "Input" && this.activeData.regValue && this.activeData.regValue.value == "number") ||
          currentLabelType == "Radio" ||
          currentLabelType == "Checkbox" ||
          currentLabelType == "Select") &&
        this.formType == "4"
      ) {
        let earlyFormsName = params.templateFormId ? "warnTemplateEarlyForms" : "warnMedicalEarlyForms";
        let earlyVosName = params.templateFormId ? "warnTemplateEarlyVOS" : "warnMedicalEarlyVOS";
        let earlyConditionFormsName = params.templateFormId ? "warnTemplateEarlyConditionForms" : "warnMedicalEarlyConditionForms";
        let earlyConditionVOSName = params.templateFormId ? "warnTemplateEarlyConditionVOS" : "warnMedicalEarlyConditionVOS";
        let warnMedicalEarlyForms = undefined;
        let warnEarlyConditionForms = [];
        if (this.activeData[earlyFormsName] && this.activeData[earlyFormsName].length) {
          warnMedicalEarlyForms = this.activeData[earlyFormsName][0];
          warnEarlyConditionForms = warnMedicalEarlyForms[earlyConditionFormsName];
        } else if (this.activeData[earlyVosName] && this.activeData[earlyVosName].length) {
          warnMedicalEarlyForms = this.activeData[earlyVosName][0];
          warnEarlyConditionForms = warnMedicalEarlyForms[earlyConditionVOSName];
        } else {
          warnMedicalEarlyForms = warningInfo(earlyConditionFormsName);
          warnEarlyConditionForms = warningInfo(earlyConditionFormsName)[earlyConditionFormsName];
        }
        if (currentLabelType == "Radio" || currentLabelType == "Checkbox" || currentLabelType == "Select") {
          if (warnEarlyConditionForms.length) {
            warnEarlyConditionForms.forEach((forItem) => {
              forItem.earlyWarnValue = Array.isArray(forItem.earlyWarnValue)
                ? forItem.earlyWarnValue
                : forItem.earlyWarnValue
                ? forItem.earlyWarnValue.split(",")
                : [];
            });
          }
          warnMedicalEarlyForms.isCondition = "";
        } else {
          if (warnEarlyConditionForms.length) {
            warnEarlyConditionForms.forEach((forItem) => {
              forItem.earlyWarnValue = forItem.earlyWarnValue ? forItem.earlyWarnValue : undefined;
            });
          }
          warnMedicalEarlyForms.isCondition = warnMedicalEarlyForms.isCondition;
          warnMedicalEarlyForms.warnPreDesc = warnMedicalEarlyForms.warnPreDesc;
        }

        this.warnEarlyConditionForms = warnEarlyConditionForms;
        warnMedicalEarlyInfo.push(warnMedicalEarlyForms);
        this.warningMedicalIsCondition = warnMedicalEarlyInfo[0].isCondition;
        this.warnPreDesc = warnMedicalEarlyInfo[0].warnPreDesc;
      }
      // console.log('warnMedicalEarlyInfo',this.warningMedicalIsCondition)
      return warnMedicalEarlyInfo;
    },
  },
  watch: {
    "activeData.min"(newVal) {
      if (newVal >= this.activeData.max) {
        this.activeData.min = this.activeData.max - 1;
      }
      if (newVal != undefined && this.activeData.options) {
        this.activeData.options.forEach((item) => {
          if (this.activeData.defaultValue.length < newVal) {
            this.activeData.defaultValue.push(item.value);
          }
          this.activeData.defaultValue = [...new Set(this.activeData.defaultValue)];
        });
      }
    },
    "activeData.max"(newVal, oldVal) {
      if (newVal <= this.activeData.min) {
        this.activeData.max = this.activeData.min + 1;
      }
    },
    "activeData.formType"(newVal) {
      // console.log('formType newVal',newVal)
      if (newVal != "2") {
        this.currentTab = "field";
      }
    },
    "activeData.startType"(newVal) {
      // console.log('newVal',newVal)
      if (newVal == "1") {
        this.endDaterangeTypeList = [daterangeTypes[0], daterangeTypes[1]];
      } else if (newVal == "3") {
        this.endDaterangeTypeList = [...daterangeTypes];
      } else if (newVal == "2") {
        this.endDaterangeTypeList = [daterangeTypes[0], daterangeTypes[2]];
      }
      this.activeData.endType = "";
    },
    "activeData.propertyCode"(newVal, oldVal) {
      let newLabelType = this.activeData.labelType;
      if (newVal != oldVal) {
        if (
          (newLabelType != "Radio" && newLabelType != "Checkbox" && this.formType == "3") ||
          (this.formType == "4" &&
            newLabelType != "Radio" &&
            newLabelType != "Checkbox" &&
            newLabelType != "Select" &&
            !(newLabelType == "Input" && this.activeData.regValue && this.activeData.regValue.value == "number"))
        ) {
          this.currentTab = "field";
        }
      }
    },
    warnEarlyConditionForms: {
      handler(newVal) {
        let params = this.$route.params;
        let earlyConditionFormsName = params.templateFormId ? "warnTemplateEarlyConditionForms" : "warnMedicalEarlyConditionForms";
        const { warnTemplateEarlyConditionForms, warnMedicalEarlyConditionForms, ...otherCondit } = this.warnMedicalEarlyInfo[0];
        this.$emit("warnEarlyChange", {
          ...otherCondit,
          [earlyConditionFormsName]: newVal,
          isCondition: this.warningMedicalIsCondition,
          warnPreDesc: this.warnPreDesc,
        });
      },
      deep: true,
    },
    warningMedicalIsCondition(newVal) {
      let params = this.$route.params;
      let earlyConditionFormsName = params.templateFormId ? "warnTemplateEarlyConditionForms" : "warnMedicalEarlyConditionForms";
      const { warnTemplateEarlyConditionForms, warnMedicalEarlyConditionForms, ...otherCondit } = this.warnMedicalEarlyInfo[0];
      this.$emit("warnEarlyChange", {
        ...otherCondit,
        [earlyConditionFormsName]: this.warnEarlyConditionForms,
        isCondition: newVal,
        warnPreDesc: this.warnPreDesc,
      });
    },
    warnPreDesc(newVal) {
      let params = this.$route.params;
      let earlyConditionFormsName = params.templateFormId ? "warnTemplateEarlyConditionForms" : "warnMedicalEarlyConditionForms";
      const { warnTemplateEarlyConditionForms, warnMedicalEarlyConditionForms, ...otherCondit } = this.warnMedicalEarlyInfo[0];
      this.$emit("warnEarlyChange", {
        ...otherCondit,
        [earlyConditionFormsName]: this.warnEarlyConditionForms,
        isCondition: this.warningMedicalIsCondition,
        warnPreDesc: newVal,
      });
    },
  },
  methods: {
    endTypeChange(event) {
      console.log("event", event, this.activeData.endType);
    },
    handleFormula() {
      // 修复：点击公式设置时，根据当前状态切换弹窗显示
      this.$emit("tag-formula", true);
    },
    addReg() {
      this.activeData.regList.push({
        pattern: "",
        message: "",
      });
    },
    addSelectItem() {
      this.activeData.options.push({
        label: "",
        value: "",
      });
    },
    addTreeItem() {
      ++this.idGlobal;
      this.dialogVisible = true;
      this.currentNode = this.activeData.options;
    },
    renderContent(h, { node, data, store }) {
      return (
        <div class="custom-tree-node">
          <span>{node.label}</span>
          <span class="node-operation">
            <i on-click={() => this.append(data)} class="el-icon-plus" title="添加"></i>
            <i on-click={() => this.remove(node, data)} class="el-icon-delete" title="删除"></i>
          </span>
        </div>
      );
    },
    append(data) {
      if (!data.children) {
        this.$set(data, "children", []);
      }
      this.dialogVisible = true;
      this.currentNode = data.children;
    },
    remove(node, data) {
      const { parent } = node;
      const children = parent.data.children || parent.data;
      const index = children.findIndex((d) => d.id === data.id);
      children.splice(index, 1);
    },
    addNode(data) {
      this.currentNode.push(data);
    },
    setOptionValue(item, val) {
      item.value = isNumberStr(val) ? +val : val;
    },
    setDefaultValue(val) {
      if (Array.isArray(val)) {
        return val.join(",");
      }
      if (["string", "number"].indexOf(val) > -1) {
        return val;
      }
      if (typeof val === "boolean") {
        return `${val}`;
      }
      return val;
    },
    onDefaultValueInput(str) {
      if (Array.isArray(this.activeData.defaultValue)) {
        // 数组
        this.$set(
          this.activeData,
          "defaultValue",
          str.split(",").map((val) => (isNumberStr(val) ? +val : val))
        );
      } else if (["true", "false"].indexOf(str) > -1) {
        // 布尔
        this.$set(this.activeData, "defaultValue", JSON.parse(str));
      } else {
        // 字符串和数字
        this.$set(this.activeData, "defaultValue", isNumberStr(str) ? +str : str);
      }
    },
    onSwitchValueInput(val, name) {
      if (["true", "false"].indexOf(val) > -1) {
        this.$set(this.activeData, name, JSON.parse(val));
      } else {
        this.$set(this.activeData, name, isNumberStr(val) ? +val : val);
      }
    },
    activeColorChange(event) {
      this.activeData.params.activeColor = event;
    },
    inactiveColorChange(event) {
      this.activeData.params.inactiveColor = event;
    },
    setTimeValue(val, type) {
      const valueFormat = type === "week" ? dateTimeFormat().date : val;
      this.$set(this.activeData, "defaultValue", null);
      this.$set(this.activeData, "value-format", valueFormat);
      this.$set(this.activeData, "format", val);
      this.$set(this.activeData, "differenceUnit", dateTypeOptions().find((f) => f.value == type).dateUnit);
      this.activeData.params.valueFormat = valueFormat;
    },
    spanChange(val) {
      this.formConf.span = val;
    },
    multipleChange(val) {
      this.$set(this.activeData, "defaultValue", val ? [] : "");
    },
    dateTypeChange(val) {
      this.setTimeValue(dateTimeFormat()[val], val);
    },
    maxDateChange(event) {
      // console.log('maxDateChange',event,this.activeData)
    },
    minDateChange(event) {
      // console.log('minDateChange',event,this.activeData)
    },
    rangeChange(val) {
      // this.$set(
      //     this.activeData,
      //     'defaultValue',
      //     val ? [this.activeData.min, this.activeData.max] : this.activeData.min
      // )
    },
    rateTextChange(val) {
      if (val) this.activeData["show-score"] = false;
    },
    rateScoreChange(val) {
      if (val) this.activeData["show-text"] = false;
      this.activeData.params.showScore = val;
    },
    allowHalfChange(val) {
      this.activeData.params.allowHalf = val;
    },
    colorFormatChange(val) {
      this.activeData.defaultValue = null;
      this.activeData["show-alpha"] = val.indexOf("a") > -1;
      this.activeData.renderKey = +new Date(); // 更新renderKey,重新渲染该组件
    },
    openIconsDialog(model) {
      this.iconsVisible = true;
      this.currentIconModel = model;
    },
    setIcon(val) {
      this.activeData[this.currentIconModel] = val;
    },
    // 新增
    addInput() {
      let data = JSON.parse(JSON.stringify(this.activeData));
      let newItem = data.children[data.children.length - 1];
      newItem.label = "新增组" + newItem.label.slice(-1) + data.children.length;
      newItem.value = newItem.value + 1;
      this.activeData.children.push({
        ...newItem,
      });
      // console.log(this.activeData.children);
    },
    // 新增
    addRadio(index) {
      let data = JSON.parse(JSON.stringify(this.activeData.children[index]));
      let newItem = data.options[data.options.length - 1];
      newItem.label = "新增选项" + newItem.label.slice(-1) + data.options.length;
      newItem.value = newItem.value + 1;
      this.activeData.children[index].options.push({
        ...newItem,
      });
    },
    // / 删除组
    deleteMatrixItem(index) {
      this.activeData.children.splice(index, 1);
    },
    // 删除选项
    deleteMatrixItemOption(index, ittIndex) {
      this.activeData.children[index].options.splice(ittIndex, 1);
    },
    regChange(event) {
      //   console.log('event',event,this.activeData.regList,this.regValue)
      this.activeData.regList = [event];
      if (event.value == "number") {
        this.activeData.type = "number";
      } else {
        this.activeData.type = "";
      }
    },
    // 分割线方向变化
    directionChange(event) {
      // console.log(event)
      this.activeData.class = event;
      // this.activeData.direction = event
    },
    // 题目分数为空设置
    subjectScoresChange(event) {
      if (!event && event != 0) {
        this.activeData["subjectScores"] = 0;
      }
    },
    // 标准录入项的表单编码有修改时，删除对问题组拖拽的限制
    formCodeChange() {
      if (this.activeData.sourceLibraryId) {
        this.activeData.sourceLibraryId = undefined;
        if (this.activeData.children && this.activeData.children.length) {
          this.activeData.children.forEach((forItem) => {
            forItem.sourceLibraryId = undefined;
          });
        }
      }
    },
    // 判断是否有没设置的预警规则
    isEmptyWarnEarly() {
      let warnEarlyList = this.warnEarlyConditionForms;
      let emptyIndex = -1;
      if (this.warningMedicalIsCondition == 1) {
        emptyIndex = warnEarlyList.findIndex(
          (f) =>
            !f.earlyWarnLevel ||
            !f.sexCondition ||
            !f.ageConditionType ||
            (f.ageConditionType < 6 && !f.ageEarlyWarnValue) ||
            (f.ageConditionType == 6 && (!f.ageEarlyWarnEnd || !f.ageEarlyWarnStart)) ||
            !f.conditionType ||
            (f.conditionType < 6 && !f.earlyWarnValue) ||
            (f.conditionType == 6 && (!f.earlyWarnEnd || !f.earlyWarnStart))
        );
      } else if (this.warningMedicalIsCondition == 2) {
        emptyIndex = warnEarlyList.findIndex(
          (f) =>
            !f.earlyWarnLevel ||
            !f.conditionType ||
            (f.conditionType < 6 && !f.earlyWarnValue) ||
            (f.conditionType == 6 && (!f.earlyWarnEnd || !f.earlyWarnStart))
        );
      } else {
        emptyIndex = warnEarlyList.findIndex((f) => !f.earlyWarnLevel || !f.earlyWarnValue || !f.earlyWarnValue.length);
      }
      // console.log('emptyIndex',emptyIndex)
      return emptyIndex;
    },
    // 新增预警
    addWarningList() {
      let params = this.$route.params;
      if (this.isEmptyWarnEarly() > -1) {
        this.$message.warning("现有预警规则中含有未设置完成的项");
        return;
      }
      let earlyConditionFormsName = params.templateFormId ? "warnTemplateEarlyConditionForms" : "warnMedicalEarlyConditionForms";
      let warningItem = warningInfo(earlyConditionFormsName)[earlyConditionFormsName][0];
      if (!this.warningMedicalIsCondition) {
        warningItem.earlyWarnValue = [];
      } else {
        warningItem.earlyWarnValue = "";
      }
      this.warnEarlyConditionForms.push(warningItem);
      this.$nextTick(() => {
        this.$refs.warnEarlyScrollbar.wrap.scrollTop = this.$refs.warnEarlyScrollbar.$refs.resize.scrollHeight;
      });
    },
    // 删除预警
    deleteWaringInfo(index) {
      if (this.warnEarlyConditionForms[index].id) {
        deleteMedicalCondition(this.warnEarlyConditionForms[index].id).then((res) => {
          if (res.code == 200) {
            this.$message.success("删除成功");
            this.warnEarlyConditionForms.splice(index, 1);
          }
        });
      } else {
        this.warnEarlyConditionForms.splice(index, 1);
      }
    },
    earlyWarnChange(event) {
      // console.log('event', event)
    },
  },
};
</script>

<style lang="scss" scoped>
.right-board {
  width: 400px;
  flex-shrink: 0;
  padding-top: 3px;
  background: #fff;

  .field-box {
    position: relative;
    height: calc(100vh - 150px);
    box-sizing: border-box;
    overflow: hidden;
  }

  .el-scrollbar {
    height: 100%;
  }
  .is-without-controls {
    width: 120px;
  }
  .el-slider,
  .custom_editor {
    width: 96%;
  }
}

::v-deep .right_property_form {
  padding: 20px 10px;

  .property_input {
    width: 88%;
    .el-input-group__append {
      padding-left: 15px;
      .el-select {
        width: 70px;
      }
    }
  }

  .el-form-item {
    margin-bottom: 10px;
    width: 100%;
    display: inline-flex;
    .el-form-item__label {
      padding-right: 20px;
    }
    .el-form-item__content {
      flex: 1;
    }
  }
}

.select-item {
  display: flex;
  border: 1px dashed #fff;
  box-sizing: border-box;

  & .close-btn {
    cursor: pointer;
    color: #f56c6c;
  }

  & .el-input + .el-input {
    margin-left: 4px;
  }
}

.select-item + .select-item {
  margin-top: 4px;
}

.select-item.sortable-chosen {
  border: 1px dashed #409eff;
}

.select-line-icon {
  line-height: 32px;
  font-size: 22px;
  padding: 0 4px;
  color: #777;
}

.option-drag {
  cursor: move;
}

.time-range {
  .el-date-editor {
    width: 227px;
  }

  ::v-deep .el-icon-time {
    display: none;
  }
}

.document-link {
  position: absolute;
  display: block;
  width: 26px;
  height: 26px;
  top: 0;
  left: 0;
  cursor: pointer;
  background: #409eff;
  z-index: 1;
  border-radius: 0 0 6px 0;
  text-align: center;
  line-height: 26px;
  color: #fff;
  font-size: 18px;
}

.matrix_item {
  justify-content: space-between;

  .void_item,
  .matrix_radio_item {
    border: none;
    margin-bottom: 10px;
  }

  .matrix_radio_item {
    justify-content: space-between;
  }

  .el-input {
    width: 80%;
  }

  &.matrix_radio {
    margin-bottom: 10px;
  }

  &.matrix_void {
    margin-bottom: 10px;
  }
}

.display_flex {
  display: flex;
  align-items: center;
}

::v-deep .right-panel-tabs {
  .el-tabs__nav {
    display: flex;
    width: 100%;

    .el-tabs__item {
      flex: 1;
      text-align: center;
      padding: 0;
    }
  }
}

.properties_item {
  border: 1px #ccc solid;
  margin: 10px;
  border-radius: 5px;
  color: #333;
  padding: 0 5px;

  .properties_info {
    line-height: 28px;
    font-size: 14px;
    padding: 5px 10px;

    .properties_title {
      font-size: 16px;
    }

    &:last-child {
      border-top: 1px #ccc dashed;
    }
  }
}

::v-deep .dateRange_form_item {
  color: #333;
  display: flex;
  flex-direction: column;

  .dateranger_set_tip {
    color: #999;
    line-height: 25px;
    padding: 5px 10px;
    background: #f5f5f5;
    border-radius: 5px;
  }

  .el-form-item__label {
    text-align: left;
  }

  .el-form-item__content {
    margin-left: 0 !important;

    .el-date-editor {
      width: 160px;
    }
  }

  .dateRange_form_itemInfo {
    margin-top: 10px;

    .el-select {
      width: 110px;
      margin-right: 5px;
    }

    .el-input-number {
      width: 100px;
    }

    .el-select .el-input__inner,
    .el-input-number .el-input__inner {
      padding-left: 8px;
      padding-right: 30px;
    }
  }
}

::v-deep .article_fraction {
  color: #333;
  padding: 20px 20px;

  .article_fraction_tit {
    border-left: 3px #409eff solid;
    padding-left: 10px;
    margin-left: 6px;
    background: rgba(24, 144, 255, 0.05);
    line-height: 35px;

    .article_fraction_tip {
      font-size: 13px;
      color: #999;
    }
  }

  .el-radio-group,
  .el-checkbox-group {
    display: block;
    padding: 15px 0;
  }

  .addWarning_btn {
    margin: 10px 0;
  }

  .article_fraction_item {
    display: flex;
    align-items: center;
    margin-top: 12px;
    font-size: 14px;
    padding-left: 10px;

    .article_item_text {
      margin-right: 10px;
    }

    .article_item_fraction {
      display: flex;
      justify-content: space-between;
      align-items: center;
      width: 115px;
      flex-shrink: 0;
    }
  }

  .fraction_number {
    width: 80px;

    .el-input__inner {
      padding-left: 3px;
      padding-right: 32px;
    }
  }

  .earlyWarn_input {
    margin-top: 10px;

    .earlyWarn_number {
      width: 120px;
      margin-right: 10px;

      .el-input__inner {
        padding-left: 5px;
        padding-right: 35px;
      }
    }
  }

  .warnMedical_earlyList {
    height: calc(100vh - 305px);
  }

  .earlyWarn_content {
    border: 1px #dedede dashed;
    padding: 15px 15px 20px;
    border-radius: 8px;
    margin-bottom: 15px;

    .earlyWarn_tit {
      line-height: 30px;
      text-indent: 8px;
      border-left: 2px #409eff solid;
      background: rgba(24, 144, 255, 0.04);
    }

    .earlyWarn_type {
      margin-top: 10px;
    }

    .condition_select {
      width: 110px;
    }

    .warning_deleteBtn {
      margin: 15px 90px 0;
      width: 150px;
    }
  }
}
.unit_input {
  max-width: 175px;
  margin-left: 5px;
}
</style>
<style lang="scss">
.el-date-picker {
  .el-time-panel {
    left: auto;
    right: 0;
  }
}
</style>
