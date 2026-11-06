
import Vue from 'vue'

// 表单设计器
import Tinymce from '@/views/tool/build/tinymce/index.vue'
import Paging from "@/utils/generator/component/paging/index.vue"
import Table from "@/utils/generator/component/Table/index.vue"
import ComBMI from "@/utils/generator/component/ComBMI/index.vue"
import ComTimeNumeral from "@/utils/generator/component/ComTimeNumeral/index.vue"
import ComYeaMonthDay from "@/utils/generator/component/ComYearMonthDay/index.vue"
import ComMaternity from "@/utils/generator/component/ComMaternity/index.vue"
import ComCaption from "@/utils/generator/component/ComCaption/index.vue"
import ComSpecification from "@/utils/generator/component/ComCaseSpecification/index.vue"
import ComWHR from "@/utils/generator/component/ComWHR/index.vue"


Vue.component('tinymce', Tinymce)
Vue.component('paging', Paging)
Vue.component('com-table', Table)
Vue.component('com-BMI', ComBMI)
Vue.component('com-TimeNumeral', ComTimeNumeral)
Vue.component('com-yearMonthDay', ComYeaMonthDay)
Vue.component('com-Maternity', ComMaternity)
Vue.component('com-caption', ComCaption)
Vue.component('com-specification', ComSpecification)
Vue.component('com-WHR', ComWHR)