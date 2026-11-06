<template>
    <div class="year_month_week_day_component">
        <el-date-picker v-model="dateVal" :type="type" :format="format" placeholder="请选择" clearable>
        </el-date-picker>
        <template v-if="isShowYear">
            <p class="separator">{{ separator }}</p>
            <el-input-number v-model="yearVal" :controls="false"></el-input-number>
            <p>{{ yearUnit }}</p>
        </template>
        <template v-if="isShowMonth">
            <p class="separator">{{ separator }}</p>
            <el-input-number v-model="monthVal" :controls="false"></el-input-number>
            <p>{{ monthUnit }}</p>
        </template>
        <template v-if="isShowDay">
            <p class="separator">{{ separator }}</p>
            <el-input-number v-model="dayVal" :controls="false"></el-input-number>
            <p>{{ dayUnit }}</p>
        </template>
    </div>
</template>

<script>
    export default {
        props: ['value', 'placeholder', 'separator', 'yearUnit', 'monthUnit', 'dayUnit', 'type', 'format',
            'isShowDay', 'isShowMonth', 'isShowYear'
        ],
        data() {
            return {
                dayVal: undefined,
                monthVal: undefined,
                yearVal: undefined,
                dateVal: undefined
            }
        },
        watch: {},
        created() {

        },
        methods: {

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