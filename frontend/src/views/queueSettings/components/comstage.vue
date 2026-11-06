<template>
    <div class="com_stage_content">
        <template v-if="isCustom">
            <div class="queue_stage_item classifys-item" v-for="(item, index) in stageList" :key="index"
                :ref="`item${index}`" :class="[{'stage_item_repeat':(index>0&&item.followFrequency == '1'
                &&item.followTotal&&item.followTotal> 1),},item.className]">
                <div class="stage_item_previewFrequency" v-if="item.followInterval">
                    {{item.followFrequency=="1"?item.followInterval:item.followInterval}}
                    {{item.followIntervalUnit == 1?"天": item.followIntervalUnit == 2? "周": 
                    item.followIntervalUnit == 3? "月": item.followIntervalUnit == 4? "年":''}}
                    后
                </div>
                <div class="stage_item_dom">
                    <template v-if="index>0&&item.followFrequency == '1'&&item.followTotal&&item.followTotal> 1">
                        <div class="stage_item_info stage_item_info_end">
                            <div class="stage_item_digit">{{ item.num-item.followTotal+1 }}</div>
                            <div class="stage_item_tit">
                                随访{{(item.num-item.followTotal+1)<10?"0"+(item.num-item.followTotal+1):item.num-item.followTotal+1}}
                            </div>
                        </div>
                        <div class="stage_item_infoTips">
                            <div class="stage_item_frequency" v-if="item.followInterval">
                                {{item.followFrequency=="1"?item.followInterval:item.followInterval}}
                                {{item.followIntervalUnit == 1?"天": item.followIntervalUnit == 2? "周":
                                 item.followIntervalUnit == 3? "月": item.followIntervalUnit == 4? "年":''}}
                                一次
                            </div>
                            <div class="line_dotted"></div>
                        </div>
                    </template>
                    <div class="stage_item_info">
                        <div class="stage_item_digit">{{ item.num }}</div>
                        <div class="stage_item_tit">
                            {{index==0?item.followName
                                    :'随访'+(item.num < "10" ? "0" + item.num : item.num)}}
                        </div>
                    </div>
                </div>
                <div class="stage_item_nextInfo" v-if="index<stageList.length-1">
                    <div class="line_orange_slider"></div>
                </div>
            </div>
        </template>
        <template v-else>
            <div v-for="(item,index) in stageList" :key="index" class="com_stage_item">
                <div class="com_stage_info">
                    <div class="com_stage_circle">{{index}}</div>
                    <div class="com_stage_context">{{item.followName}}</div>
                </div>
                <div class="com_stage_line" v-if="index<stageList.length-1"></div>
                <div class="com_load_timeBom" v-if="item.followInterval">
                    术后
                    {{item.followIntervalStr}}
                    {{item.followIntervalUnit == 1?"天": item.followIntervalUnit == 2? "周":
                    item.followIntervalUnit == 3? "月": item.followIntervalUnit == 4? "年": ""}}
                </div>
            </div>
        </template>

    </div>
</template>

<script>
    export default {
        props: ['stageList', 'isCustom'],
        data() {
            return {};
        },
        computed: {},
        watch: {
            stageList: {
                handler(newVal) {
                    if (newVal && newVal.length) {
                        newVal.forEach((item, index) => {
                            // <!-- 随访不限制次数开始  followFrequency 1-限制次数 2-不限次数-->
                            item['followIntervalStr'] = index > 1 ? index - 1 : item.followInterval

                            let lastItem = index < newVal.length - 1 ? newVal[index + 1] : item
                            let preItem = index > 0 ? newVal[index - 1] : item
                            if (preItem.frequencyType == '1' && item.frequencyType == '1' && lastItem
                                .frequencyType == '2') {
                                item.className = 'pre1_self1_last2'
                            } else if (preItem.frequencyType == '1' && item.frequencyType == '1' && lastItem
                                .frequencyType == '1') {
                                item.className = 'pre1_self1_last1'
                            } else if (preItem.frequencyType == '1' && item.frequencyType == '2' && lastItem
                                .frequencyType == '1') {
                                item.className = 'pre1_self2_last1'
                            } else if (preItem.frequencyType == '1' && item.frequencyType == '2' && lastItem
                                .frequencyType == '2') {
                                item.className = 'pre1_self2_last2'
                            } else if (preItem.frequencyType == '2' && item.frequencyType == '1' && lastItem
                                .frequencyType == '1') {
                                item.className = 'pre2_self1_last1'
                            } else if (preItem.frequencyType == '2' && item.frequencyType == '1' && lastItem
                                .frequencyType == '2') {
                                item.className = 'pre2_self1_last2'
                            } else if (preItem.frequencyType == '2' && item.frequencyType == '2' && lastItem
                                .frequencyType == '1') {
                                item.className = 'pre2_self2_last1'
                            } else if (preItem.frequencyType == '2' && item.frequencyType == '2' && lastItem
                                .frequencyType == '2') {
                                item.className = 'pre2_self2_last2'
                            } else if (preItem.frequencyType == '1' && item.frequencyType == '2') {
                                item.className = 'pre1_self2'
                            } else if (preItem.frequencyType == '2' && item.frequencyType == '1') {
                                item.className = 'pre2_self1'
                            } else if (preItem.frequencyType == '2' && item.frequencyType == '12') {
                                item.className = 'pre2_self2'
                            }
                        })
                    }
                },
                deep: true,
            },
        },
        created() {},
        methods: {},
    };
</script>

<style lang="scss" scoped>
    .com_stage_content {
        width: 100%;
        padding: 20px 0;
        display: flex;

        .com_stage_item {
            width: 300px;
            font-size: 14px;
            font-weight: 400;
            text-align: center;
            flex-wrap: nowrap;
            color: #409eff;
            flex-shrink: 0;
            position: relative;
            justify-content: center;

            .com_stage_info {
                display: flex;
                flex-direction: column;
                align-items: center;

                .com_stage_circle {
                    width: 40px;
                    height: 40px;
                    line-height: 36px;
                    border-radius: 50%;
                    border: 2px solid #409eff;
                    background-color: #fff;
                    font-size: 18px;
                    font-weight: bold;
                    background-color: #fff;
                }

                .com_stage_context {
                    margin-top: 8px;
                    text-align: center;
                    color: #333;
                }

            }

            .com_stage_line {
                width: 230px;
                height: 2px;
                display: inline-block;
                border-bottom: 2px solid #ffc35a;
                position: absolute;
                top: 32%;
                right: -112px;

                &::before {
                    content: "";
                    width: 10px;
                    height: 10px;
                    background: rgb(255 195 90);
                    border-radius: 50%;
                    position: absolute;
                    left: 0;
                    top: -4px;
                }

                &::after {
                    content: "";
                    width: 0px;
                    height: 0px;
                    border: 8px solid #000;
                    border-left-color: rgb(255 195 90);
                    border-bottom-color: transparent;
                    border-top-color: transparent;
                    border-right-color: transparent;
                    position: absolute;
                    right: -12px;
                    top: -7px;
                }
            }

            .com_load_timeBom {
                font-weight: bold;
                position: absolute;
                left: -20px;
                top: -3px;
            }
        }

        .queue_stage_item {
            width: 300px;
            font-size: 14px;
            font-weight: 400;
            white-space: nowrap;
            color: #333;
            display: flex;
            text-align: center;
            position: relative;
            flex-shrink: 0;

            .stage_item_previewFrequency {
                position: absolute;
                left: -190px;
                width: 120px;
                color: #409eff;
                font-weight: bold;
            }

            .stage_item_dom {
                display: flex;
                justify-content: space-between;
                align-items: flex-start;
            }

            &.stage_item_repeat {
                .stage_item_dom {
                    width: 170px;
                }
            }

            .stage_item_info {
                display: flex;
                flex-direction: column;
                align-items: center;

                .stage_item_digit {
                    width: 40px;
                    height: 40px;
                    line-height: 35px;
                    border-radius: 50%;
                    color: #409eff;
                    border: 2px solid #409eff;
                    font-size: 20px;
                    font-weight: bold;
                }

                .stage_item_tit {
                    margin-top: 10px;
                    margin-bottom: 10px;
                }
            }

            .stage_item_infoTips {
                flex: 1;

                .stage_item_frequency {
                    width: 60%;
                    margin: 0 auto 0;
                    font-weight: bold;
                    color: #5c96ee;
                }

                .line_dotted {
                    width: 96%;
                    margin: 3px 2% 0;
                    border-bottom: 2px dashed #409eff;
                }

            }

            .stage_item_nextInfo {
                flex: 1;
            }

            .line_orange_slider {
                position: relative;
                width: 90%;
                height: 2px;
                display: inline-block;
                border-bottom: 2px solid rgb(255 195 90);
                margin: 20px 6% 0 4%;

                &::before {
                    content: "";
                    width: 10px;
                    height: 10px;
                    background: rgb(255 195 90);
                    border-radius: 50%;
                    position: absolute;
                    left: 0;
                    top: -4px;
                }

                &::after {
                    content: "";
                    width: 0px;
                    height: 0px;
                    border: 8px solid #000;
                    border-left-color: rgb(255 195 90);
                    border-bottom-color: transparent;
                    border-top-color: transparent;
                    border-right-color: transparent;
                    position: absolute;
                    right: -12px;
                    top: -7px;
                }

            }

            &.pre1_self1_last2 {
                width: 237px;
            }

            &.pre2_self2_last1 {
                width: 362px;

                .stage_item_previewFrequency {
                    left: -125px;
                }
            }

            &.pre1_self2_last2,
            &.pre2_self1 {
                width: 298px;

                .stage_item_previewFrequency {
                    left: -155px;
                }
            }

            &.pre2_self2_last2 {
                .stage_item_previewFrequency {
                    left: -125px;
                }
            }

            &.pre2_self1_last2 {
                width: 240px;

                .stage_item_previewFrequency {
                    left: -155px;
                }
            }

            &.pre2_self1_last1 {
                width: 286px;
            }

            &.pre1_self2_last1 {
                width: 360px;

                .stage_item_previewFrequency {
                    left: -155px;
                }
            }
        }
    }
</style>