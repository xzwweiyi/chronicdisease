<template>
    <div class="time_numeral_component">
        <div class="time_numeral_datePicker">
            <span class="datePicker_label">{{ startDateLabel }}</span>
            <el-date-picker v-model="startDateValue" :type="type" :placeholder="placeholder" :value-format="valueFormat"
                @change="timeChange($event,1)" :picker-options="startPickerOptions">
            </el-date-picker>
        </div>
        <template v-if="isSetEndTime">
            <div class="time_numeral_datePicker">
                <span class="datePicker_label">{{ endDateLabel }}</span>
                <el-date-picker v-model="endDateValue" :type="type" :placeholder="placeholder"
                    :value-format="valueFormat" @change="timeChange($event,2)" :picker-options="endPickerOptions">
                </el-date-picker>
            </div>
        </template>
        <div class="difference_container">
            <span class="dif_str">{{ differenceLabel }}</span>
            <span class="dif_val">{{ differenceValue }}</span>
            <!-- <el-input-number v-else v-model="differenceValue" :controls="false" :precision="precision"
                @change="timeChange($event,3)"></el-input-number> -->
            <span class="dif_str">{{ differenceUnit }}</span>
        </div>
    </div>
</template>

<script>
export default {
    props: ['placeholder', 'unit', 'type', 'isSetEndTime', 'startDateLabel', 'endDateLabel', 'differenceLabel',
        'differenceUnit', 'valueFormat'],
    data() {
        return {
            comNumber: undefined,
            precision: undefined,
            startDateValue: '',
            endDateValue: '',
            differenceValue: '',
        }
    },
    computed: {
        startPickerOptions() {
            let _that = this
            let endDate = '';
            if (this.isSetEndTime) {
                endDate = this.endDateValue
            } else {
                endDate = new Date()
            }
            return {
                disabledDate(time) {
                    if (endDate) {
                        if (_that.type == 'date') {
                            return time.getTime() > endDate.getTime();
                        } else if (_that.type == 'month') {
                            return _that.convertMonthDate(time) > _that.convertMonthDate(endDate);
                        } else if (_that.type == 'year') {
                            return time.getFullYear() > endDate.getFullYear();
                        }
                    } else {
                        return false
                    }
                },
            }
        },
        endPickerOptions() {
            let startDate = this.startDateValue;
            let _that = this
            return {
                disabledDate(time) {
                    if (startDate) {
                        if (_that.type == 'date') {
                            return time.getTime() < startDate.getTime();
                        } else if (_that.type == 'month') {
                            return _that.convertMonthDate(time) < _that.convertMonthDate(startDate);
                        } else if (_that.type == 'year') {
                            return time.getFullYear() < startDate.getFullYear();
                        }
                    } else {
                        return false
                    }
                },
            }
        }
    },
    watch: {
        type(newVal) {
            this.startDateValue = '';
            this.endDateValue = '';
            this.differenceValue = '';
        },
    },
    created() {

    },
    methods: {
        timeChange(event, changeType) {
            let type = this.type
            if (event) {
                if (changeType == '1') {
                    if (event && this.isSetEndTime && this.endDateValue) {
                        this.convertDifferenceVal(event, this.endDateValue, type)
                    } else if (event && !this.isSetEndTime) {
                        this.convertDifferenceVal(event, null, type)
                    } else {
                        this.differenceValue = ''
                    }
                } else if (changeType == '2') {
                    if (event && this.isSetEndTime && this.startDateValue) {
                        this.convertDifferenceVal(this.startDateValue, event, type)
                    } else {
                        this.differenceValue = ''
                    }
                } else if (changeType == '3' && !this.isSetEndTime && !this.startDateValue) {
                    // let currentDate = new Date();
                    // let eventVal = 0;
                    // let startValue = ''
                    // if(type == 'date'){
                    //     eventVal =  event* 1000 * 60 * 60 * 24
                    //     startValue = currentDate.getTime() - eventVal
                    //     startValue = new Date(startValue)
                    //     startValue = startValue.getFullYear()+'-'+(startValue.getMonth()+1)+'-'+startValue.getDate()
                    // }else if(type == 'month'){
                    //     eventVal = event* 1000 * 60 * 60 * 24*30
                    //     startValue = currentDate.getTime() - eventVal
                    //     startValue = new Date(startValue)
                    //     startValue = startValue.getFullYear()+'-'+(startValue.getMonth()+1)
                    // }else if(type == 'year'){
                    //     eventVal = event* 1000 * 60 * 60 * 24*30*12
                    //     startValue = currentDate.getTime() - eventVal
                    //     startValue = new Date(startValue)
                    //     startValue = startValue.getFullYear()
                    // }                     
                    // this.startDateValue = startValue
                }
            }
        },
        convertDifferenceVal(startDate, endDate) {
            let type = this.type
            let startDateTime = new Date(startDate)
            let endDateTime = ''
            if (endDate) {
                endDateTime = new Date(endDate)
            } else {
                let newdate = new Date()
                let year = newdate.getFullYear()  // $)A5C5=51G0Dj7] 
                let month = newdate.getMonth() + 1 // $)A5C5=51G0TB7]#(0-11TB7]#,+1JG51G0TB7]#)
                month = month > 10 ? month : '0' + month
                endDateTime = type == 'date' ? newdate : type == 'month' ? new Date(year + '-' + month + '-01')
                    : type == 'year' ? new Date(year + '-01') : new Date()
            }
            let differenceVal = endDateTime.getTime() - startDateTime.getTime()
            if (type == 'date') {
                differenceVal = Number(differenceVal / 1000 / 60 / 60 / 24)
            } else if (type == 'month') {
                differenceVal = Number(differenceVal / 1000 / 60 / 60 / 24 / 30)
            } else if (type == 'year') {
                differenceVal = Number(endDateTime.getFullYear() - startDateTime.getFullYear())
            }
            this.differenceValue = Math.trunc(differenceVal)
        },
        convertMonthDate(value) {
            let date = new Date(value)
            let year = date.getFullYear()
            let month = date.getMonth() + 1
            month = month < 10 ? ('0' + month) : month
            let curTimeStr = year + "-" + month
            let curTime = new Date(curTimeStr)
            return curTime.getTime()
        },
    }
}
</script>

<style lang="scss" scoped>
.time_numeral_component {
    display: inline-flex;
    flex-wrap: wrap;
    align-items: center;
    font-size: 15px;
    .time_numeral_datePicker {
        display: flex;
        flex-shrink: 0;
        margin-bottom: 5px;
        .datePicker_label {
            flex-shrink: 0;
            color: #666;
            margin-right: 3px;
        }
    }
    ::v-deep .el-date-editor {
        width: 140px !important;
        margin-right: 10px;
    }

    ::v-deep .el-input-number.is-without-controls {
        width: 80px !important;
    }
    .difference_container {
        flex-shrink: 0;
        display: flex;
        align-items: center;
        margin-bottom: 3px;
        .dif_str,
        .dif_val {
            flex-shrink: 0;
            color: #666;
        }
        .dif_val {
            border: 1px #ccc solid;
            min-width: 40px;
            height: 30px;
            border-radius: 3px;
            margin: 0 5px;
            display: flex;
            justify-content: center;
            align-items: center;
        }
    }
}
</style>