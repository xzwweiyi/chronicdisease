import Cookies from 'js-cookie'

const state = {
    sidebar: {
        opened: Cookies.get('sidebarStatus') ? !!+Cookies.get('sidebarStatus') : true,
        withoutAnimation: false,
        hide: false
    },
    device: 'desktop',
    size: Cookies.get('size') || 'default',
    medicalTreeInfo: {
        firstMenus: undefined,
        secondMenus: undefined,
        thirdMenus: undefined
    },
}

const mutations = {
    TOGGLE_SIDEBAR: state => {
        state.sidebar.opened = !state.sidebar.opened
        state.sidebar.withoutAnimation = false
        if (state.sidebar.opened) {
            Cookies.set('sidebarStatus', 1)
        } else {
            Cookies.set('sidebarStatus', 0)
        }
    },
    CLOSE_SIDEBAR: (state, withoutAnimation) => {
        Cookies.set('sidebarStatus', 0)
        state.sidebar.opened = false
        state.sidebar.withoutAnimation = withoutAnimation
    },
    TOGGLE_DEVICE: (state, device) => {
        state.device = device
    },
    SET_SIZE: (state, size) => {
        state.size = size
        Cookies.set('size', size)
    },
    SET_SIDEBAR_HIDE: (state, status) => {
        state.sidebar.hide = status
    },
    SET_MEDICAL_FIRST_ITEM: (state, medicalFirstItem) => {
        state.medicalTreeInfo.firstMenus = medicalFirstItem
        Cookies.set('medicalTreeInfo', JSON.stringify(state.medicalTreeInfo))
        Cookies.set('medicalTreeInfo.firstMenus', JSON.stringify(medicalFirstItem))
    },
    SET_MEDICAL_THIRD_ITEM: (state, medicalThirdItem) => {
        state.medicalTreeInfo.thirdMenus = medicalThirdItem
        Cookies.set('medicalTreeInfo', JSON.stringify(state.medicalTreeInfo))
        Cookies.set('medicalTreeInfo.thirdMenus', JSON.stringify(medicalThirdItem))
    },
    SET_MEDICAL_SECOND_ITEM: (state, medicalSecondItem) => {
        state.medicalTreeInfo.secondMenus = medicalSecondItem
        Cookies.set('medicalTreeInfo', JSON.stringify(state.medicalTreeInfo))
        Cookies.set('medicalTreeInfo.secondMenus', JSON.stringify(medicalSecondItem))
    },
}

const actions = {
    toggleSideBar({ commit }) {
        commit('TOGGLE_SIDEBAR')
    },
    closeSideBar({ commit }, { withoutAnimation }) {
        commit('CLOSE_SIDEBAR', withoutAnimation)
    },
    toggleDevice({ commit }, device) {
        commit('TOGGLE_DEVICE', device)
    },
    setSize({ commit }, size) {
        commit('SET_SIZE', size)
    },
    toggleSideBarHide({ commit }, status) {
        commit('SET_SIDEBAR_HIDE', status)
    },
    setMedicalFirst({ commit }, { medicalFirstItem }) {
        commit('SET_MEDICAL_FIRST_ITEM', medicalFirstItem)
    },
    setMedicalThird({ commit }, { medicalThirdItem }) {
        commit('SET_MEDICAL_THIRD_ITEM', medicalThirdItem)
    },
    setMedicalSeconds({ commit }, { medicalSecondItem }) {
        commit('SET_MEDICAL_SECOND_ITEM', medicalSecondItem)
    }
}

export default {
    namespaced: true,
    state,
    mutations,
    actions
}