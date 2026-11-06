// 
export const userDataOne = [
    {userId: '1', type: '11', userName: '王五', stage: '高血压轻度患者队列', iphone: '13412345678', createTime: '2022-07-22' },
    {userId: '2', type: '11', userName: '范在', stage: '高血压轻度患者队列', iphone: '13412345678', createTime: '2022-07-22' },
    {userId: '3', type: '11', userName: '果然', stage: '高血压轻度患者队列', iphone: '13412345678', createTime: '2022-07-22' },
    {userId: '4', type: '11', userName: '如此', stage: '高血压轻度患者队列', iphone: '13412345678', createTime: '2022-07-22' },
    {userId: '5', type: '11', userName: '后天', stage: '高血压轻度患者队列', iphone: '13412345678', createTime: '2022-07-22' }
]
export const userDataTwo = [
    {userId: '7', type: '12', userName: '王五', stage: '高血压轻度患者队列', iphone: '13412345678', createTime: '2022-07-22' },
    {userId: '8', type: '12', userName: '范在', stage: '高血压轻度患者队列', iphone: '13412345678', createTime: '2022-07-22' },
    {userId: '9', type: '12', userName: '果然', stage: '高血压轻度患者队列', iphone: '13412345678', createTime: '2022-07-22' },
]

export const userDataThree = [
    {userId: '10', type: '3', userName: '王五', stage: '高血压轻度患者队列', iphone: '13412345678', createTime: '2022-07-22' },
    {userId: '11', type: '3', userName: '范在', stage: '高血压轻度患者队列', iphone: '13412345678', createTime: '2022-07-22' },
    {userId: '12', type: '3', userName: '果然', stage: '高血压轻度患者队列', iphone: '13412345678', createTime: '2022-07-22' },
]

export const userDataFour = [
    {userId: '13', type: '4', userName: '王五', stage: '高血压轻度患者队列', iphone: '13412345678', createTime: '2022-07-22' },
    {userId: '14', type: '4', userName: '范在', stage: '高血压轻度患者队列', iphone: '13412345678', createTime: '2022-07-22' },
    {userId: '15', type: '4', userName: '果然', stage: '高血压轻度患者队列', iphone: '13412345678', createTime: '2022-07-22' },
]
// 随访超窗
export const userDataFive = [
    {userId: '16', type: '5', userName: '王五', stage: '高血压轻度患者队列', iphone: '13412345678', createTime: '2022-07-22' },
    {userId: '17', type: '5', userName: '范在', stage: '高血压轻度患者队列', iphone: '13412345678', createTime: '2022-07-22' },
    {userId: '18', type: '5', userName: '果然', stage: '高血压轻度患者队列', iphone: '13412345678', createTime: '2022-07-22' },
    {userId: '19', type: '5', userName: '如此', stage: '高血压轻度患者队列', iphone: '13412345678', createTime: '2022-07-22' },
    {userId: '20', type: '5', userName: '后天', stage: '高血压轻度患者队列', iphone: '13412345678', createTime: '2022-07-22' },
]

// 周期
export const deptOptions = [
    {id: '1', label: '高血压',  number:'99+',
        children: [
            {id: '101', label: '高血压轻度患者队列', number:'66', children: [
                    {id: '10110', type: '11', label: '待评价'},
                    {id: '10110', type: '12', label: '已完成'}
                ]
            },
            {id: '102', label: '高血压中度患者队列',  number:'22', children: [
                    {id: '10110', type: '11', label: '待评价'},
                    {id: '10110', type: '12', label: '已完成'}
                ]
            },
            {id: '103', label: '高血压重度患者队列',  number:'21', children: [
                    {id: '10110', type: '11', label: '待评价'},
                    {id: '10110', type: '12', label: '已完成'}
                ]
            }
        ]
    },
    {id: '2', label: '冠心病', 
        children: [
            {id: '101', label: '队列1', children: [
                    {id: '1011', label: '患者入组审核', children:[
                            {id: '10110', type: '11', label: '待评价'},
                            {id: '10110', type: '12', label: '已完成'},
                        ]
                    
                    }
                ]
            },
            {id: '101', label: '队列2', children: [
                    {id: '1011', label: '患者入组审核', children:[
                            {id: '10110', type: '11', label: '待评价'},
                            {id: '10110', type: '12', label: '已完成'},
                        ]
                    
                    }
                ]
            },
            {id: '101', label: '队列3', children: [
                    {id: '1011', label: '患者入组审核', children:[
                            {id: '10110', type: '11', label: '待评价'},
                            {id: '10110', type: '12', label: '已完成'},
                        ]
                    
                    }
                ]
            }
        ]
    }
]
// 预警模块
export const tableData = [
    { name: "血小板计数", value: "19", rule: '超大区间' },
    { name: "血浆凝血酶原时", value: "30", rule: '大于标准值' },
    { name: "PTE/DVT症状", value: "加重", rule: '选择加重' },
]