// 
export const userDataOne = [
    {userId: '1', type: '1', userName: '王五', stage: '高血压轻度患者队列', iphone: '13412345678', createTime: '2022-07-22' },
    {userId: '2', type: '1', userName: '范在', stage: '高血压轻度患者队列', iphone: '13412345678', createTime: '2022-07-22' },
    {userId: '3', type: '1', userName: '果然', stage: '高血压轻度患者队列', iphone: '13412345678', createTime: '2022-07-22' },
    {userId: '4', type: '1', userName: '如此', stage: '高血压轻度患者队列', iphone: '13412345678', createTime: '2022-07-22' },
    {userId: '5', type: '1', userName: '后天', stage: '高血压轻度患者队列', iphone: '13412345678', createTime: '2022-07-22' }
]
export const userDataTwo = [
    {userId: '7', type: '2', userName: '王五', stage: '高血压轻度患者队列', iphone: '13412345678', createTime: '2022-07-22' },
    {userId: '8', type: '2', userName: '范在', stage: '高血压轻度患者队列', iphone: '13412345678', createTime: '2022-07-22' },
    {userId: '9', type: '2', userName: '果然', stage: '高血压轻度患者队列', iphone: '13412345678', createTime: '2022-07-22' },
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
                    {id: '1011', type: '1', label: '患者入组审核', number:'08', children:[
                            {id: '10110', type: '1', label: '待处理'},
                            {id: '10110', type: '1', label: '进行中'},
                            {id: '10110', type: '1', label: '已完成'},
                        ]
                    
                    },
                    {id: '1012', type: '2', label: '随访处理', number:'22', children:[
                            {id: '10110', type: '2', label: '待处理'},
                            {id: '10110', type: '2', label: '进行中'},
                            {id: '10110', type: '2', label: '已完成'},
                        ]
                    
                    },
                    {id: '1013', type: '3', label: '随访超窗', number:'25', children:[
                            {id: '10110', type: '3', label: '待处理'},
                            {id: '10110', type: '3', label: '进行中'},
                            {id: '10110', type: '3', label: '已完成'},
                        ]
                    
                    },
                    {id: '1014', type: '4', label: '预警处理', number:'28', children:[
                            {id: '10110', type: '4', label: '待处理'},
                            {id: '10110', type: '4', label: '进行中'},
                            {id: '10110', type: '4', label: '已完成'},
                        ]
                    
                    }
                ]
            },
            {id: '102', label: '高血压中度患者队列', number:'66', children: [
                    {id: '1021', label: '患者入组审核', children:[
                            {id: '10110', label: '待处理'},
                            {id: '10110', label: '已完成'},
                        ]
                    
                    }
                ]
            },
            {id: '103', label: '高血压重度患者队列', number:'86', children: [
                    {id: '1031', label: '患者入组审核', children:[
                            {id: '10110', label: '待处理'},
                            {id: '10110', label: '已完成'},
                        ]
                    
                    }
                ]
            }
        ]
    },
    {id: '2', label: '冠心病', number:'99+',
        children: [
            {id: '101', label: '队列1', children: [
                    {id: '1011', label: '患者入组审核', children:[
                            {id: '10110', label: '待处理'},
                            {id: '10110', label: '已完成'},
                        ]
                    
                    }
                ]
            },
            {id: '101', label: '队列2', children: [
                    {id: '1011', label: '患者入组审核', children:[
                            {id: '10110', label: '待处理'},
                            {id: '10110', label: '已完成'},
                        ]
                    
                    }
                ]
            },
            {id: '101', label: '队列3', children: [
                    {id: '1011', label: '患者入组审核', children:[
                            {id: '10110', label: '待处理'},
                            {id: '10110', label: '已完成'},
                        ]
                    
                    }
                ]
            }
        ]
    },
    {id: '3', label: '待分配患者', number:'99+', 
        children: [
            {id: '101', label: '队列1', children: [
                    {id: '1011', label: '患者入组审核', children:[
                            {id: '10110', label: '待处理'},
                            {id: '10110', label: '已完成'},
                        ]
                    
                    }
                ]
            },
            {id: '101', label: '队列2', children: [
                    {id: '1011', label: '患者入组审核', children:[
                            {id: '10110', label: '待处理'},
                            {id: '10110', label: '已完成'},
                        ]
                    
                    }
                ]
            },
            {id: '101', label: '队列3', children: [
                    {id: '1011', label: '患者入组审核', children:[
                            {id: '10110', label: '待处理'},
                            {id: '10110', label: '已完成'},
                        ]
                    
                    }
                ]
            }
        ]
    }
]
// 随访
export const nameOptions = [
    { name: "天", value: "1" },
    { name: "周", value: "2" },
    { name: "月", value: "3" },
]