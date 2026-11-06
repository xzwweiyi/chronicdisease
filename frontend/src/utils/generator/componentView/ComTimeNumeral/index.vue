<template>
    <div class="time_numeral_component">
        <div class="time_numeral_datePicker">
            <span class="datePicker_label">{{ startDateLabel }}</span>
            <span class="datePicker_label">{{ startDateValue | nullFormat}}</span>
        </div>
        <template v-if="isSetEndTime">
            <div class="time_numeral_datePicker">
                <span class="datePicker_label">{{ endDateLabel }}</span>
                <span class="datePicker_label">{{ endDateValue | nullFormat}}</span>
            </div>
        </template>
        <div class="difference_container">
            <span class="dif_str">{{ differenceLabel }}</span>
            <span class="dif_val">{{ differenceValue | nullFormat }}</span>
            <span class="dif_str">{{ differenceUnit }}</span>
        </div>
    </div>
</template>

<script>
export default {
    props: ['info', 'isSetEndTime', 'startDateLabel', 'endDateLabel', 'differenceLabel',
        'differenceUnit', 'defaultValue'],
    data() {
        return {
            comNumber: undefined,
            precision: undefined,
            startDateValue: '',
            endDateValue: '',
            differenceValue: '',
        }
    },
    computed: {},
    watch: {
        info: {
            immediate: true,
            handler(newVal) {
                if (newVal) {
                    console.log(newVal)
                    this.startDateValue = newVal.start
                    this.endDateValue = newVal.end
                    this.differenceValue = newVal.difference
                }
            }
        },
    },
    created() {

    },
    methods: {
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
        align-items: flex-start;
        margin-right: 10px;
        margin-bottom: 8px;
        .datePicker_label {
            flex-shrink: 0;
            color: #666;
            margin-right: 3px;
        }
    }
    .difference_container {
        flex-shrink: 0;
        display: flex;
        align-items: center;
        margin-bottom: 8px;
        .dif_str,
        .dif_val {
            flex-shrink: 0;
            color: #666;
        }
        .dif_val {
            margin: 0 5px;
        }
    }
}
</style>