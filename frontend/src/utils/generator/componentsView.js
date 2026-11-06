
import Vue from 'vue'

// 表单设计器
import Tinymce from '@/views/tool/build/tinymce/index.vue'
import Paging from "@/utils/generator/componentView/paging/index.vue"
import Table from "@/utils/generator/componentView/Table/index.vue"
import ComBMI from "@/utils/generator/componentView/ComBMI/index.vue"
import ComTimeNumeral from "@/utils/generator/componentView/ComTimeNumeral/index.vue"
import ComDValue from "@/utils/generator/componentView/ComDValue/index.vue"
import ComYeaMonthDay from "@/utils/generator/componentView/ComYearMonthDay/index.vue"
import ComCaption from "@/utils/generator/componentView/ComCaption/index.vue"
import ComSpecification from "@/utils/generator/componentView/ComCaseSpecification/index.vue"



Vue.component('view-table', Table)
Vue.component('view-BMI', ComBMI)
Vue.component('view-TimeNumeral', ComTimeNumeral)
Vue.component('view-DValue',ComDValue)
Vue.component('view-yearMonthDay', ComYeaMonthDay)
Vue.component('view-caption', ComCaption)
Vue.component('view-specification', ComSpecification)
