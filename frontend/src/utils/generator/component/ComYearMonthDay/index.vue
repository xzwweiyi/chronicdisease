<template>
    <div class="year_month_week_day_component">
        <el-date-picker v-model="dateVal" :type="type" placeholder="请选择" @change="valueChange($event,1)" clearable :value-format="valueFormat" :format="format" :key="rendeerKey" 
        :disabled="!!(yearVal||monthVal||dayVal)">
        </el-date-picker>
        <template v-if="isShowYear">
            <p class="separator">{{ separator }}</p>
            <el-input-number v-model="yearVal" :controls="false" @change="valueChange($event,2)"
            :disabled="!!(dateVal||monthVal||dayVal)"></el-input-number>
            <p>{{ yearUnit }}</p>
        </template>
        <template v-if="isShowMonth">
            <p class="separator">{{ separator }}</p>
            <el-input-number v-model="monthVal" :controls="false" @change="valueChange($event,3)"
            :disabled="!!(dateVal||yearVal||dayVal)"></el-input-number>
            <p>{{ monthUnit }}</p>
        </template>
        <template v-if="isShowDay">
            <p class="separator">{{ separator }}</p>
            <el-input-number v-model="dayVal" :controls="false" @change="valueChange($event,4)"
            :disabled="!!(dateVal||monthVal||yearVal)"></el-input-number>
            <p>{{ dayUnit }}</p>
        </template>
    </div>
</template>

<script>
export default {
    props: ['value', 'placeholder', 'separator', 'yearUnit', 'monthUnit', 'dayUnit', 'type', 'format',
        'isShowDay', 'isShowMonth', 'isShowYear', 'defaultValue', 'valueFormat'
    ],
    data() {
        return {
            dayVal: undefined,
            monthVal: undefined,
            yearVal: undefined,
            dateVal: undefined,
            rendeerKey:new Date().getTime()+''
        }
    },
    watch: {
        defaultValue: {
            immediate: true,
            handler(newVal, oldVal) {
                if (newVal) {
                    if (newVal.specialComponentType == 1) {
                        this.dateVal = newVal.value
                    } else if (newVal.specialComponentType == 2) {
                        this.yearVal = newVal.value
                    }  else if (newVal.specialComponentType == 3) {
                        this.monthVal = newVal.value
                    }else if (newVal.specialComponentType == 4) {
                        this.dayVal = newVal.value
                    }
                    this.rendeerKey = new Date().getTime()+''
                }
            }
        }
    },
    created() {

    },
    methods: {
        valueChange(event, type) {
            let value = event
            let returnVal = undefined
            if(event){
                returnVal = {
                    specialComponentType: event ? type : undefined,
                    value
                }
                returnVal = JSON.stringify(returnVal)
            }
            this.$emit('value-change', returnVal)
        }
    }
}
</script>

<style lang="scss" scoped>
    .year_month_week_day_component {
        display: flex;
        flex-wrap: wrap;
        align-items: center;

        .separator {
            margin-left: 5px;
        }

        .el-date-editor {
            width: 120px;
            margin-bottom: 8px;
        }

        .el-input-number,
        p {
            margin-bottom: 8px;
        }

        .el-date-editor--datetime {
            width: 190px;
        }

        .el-date-editor--date {
            width: 140px;
        }

        .el-date-editor--year {
            width: 100px;
        }

        .el-date-editor--week {
            width: 150px;
        }

        .el-date-editor--month {
            width: 120px;
        }

        ::v-deep .el-input-number.is-without-controls {
            width: 70px !important;
        }

    }
</style>