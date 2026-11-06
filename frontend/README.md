## 开发

```bash
# 克隆项目
git clone https://gitee.com/y_project/RuoYi-Vue

# 进入项目目录
cd ruoyi-ui

# 安装依赖
npm install

# 建议不要直接使用 cnpm 安装依赖，会有各种诡异的 bug。可以通过如下操作解决 npm 下载速度慢的问题
npm install --registry=https://registry.npm.taobao.org

# 启动服务
npm run dev
```

浏览器访问 http://localhost:80

## 发布

```bash
# 构建测试环境
npm run build:stage

# 构建生产环境
npm run build:prod
```

``` 随访工作站列表
/**
 * 患者入组审核： ( nodeType==1 && nodeTypeStatus!=3 )
 *   患者ID、患者、计划加入队列、渠道、创建时间、操作(查看健康档案)
 * 患者入组审核：已完成    (nodeType==1&&nodeTypeStatus==3)
 *   患者ID、患者、计划加入队列、创建时间、完成时间、处理结果、操作(查看健康档案)
 * 
 * 待分配患者     ( nodeLevelType==2 )
 *   患者ID、患者、计划加入队列、创建时间、操作(查看健康档案、分配队列)
 * 待分配患者 已完成     ( nodeLevelType==2 &&nodeTypeStatus==3)
 *   患者ID、患者、计划加入队列、创建时间、完成时间、处理结果、操作(查看健康档案)
 * 
 * 随访处理   ( nodeType==2 )
 *   患者ID、患者、队列、阶段、创建时间、操作(查看随访)
 * 随访处理  进行中  ( nodeType==2 &&nodeTypeStatus==2 )
 *   患者ID、患者、队列、阶段、创建时间、质疑时间、操作(查看随访)
 * 随访处理  已完成 ( nodeType==2 &&nodeTypeStatus==3 )
 *   患者ID、患者、队列、阶段、创建时间、完成时间、操作(查看随访)
 * 
 * 随访超窗  ( nodeType==3 )
 *   患者ID、患者、阶段、联系电话、创建时间、智能外呼结果、操作(查看随访 查看外呼 更新随访时间 忽略本次 失访)
 * 随访超窗  已完成  ( nodeType==3  &&nodeTypeStatus==3 )
 *   患者ID、患者、阶段、联系电话、完成时间、智能外呼、处理结果、操作(查看随访 查看外呼)
 * 
 * 预警处理  ( nodeType==4 )
 *   患者ID、患者、阶段、创建时间、预警等级、操作(预警处理)
 * 预警处理   进行中  ( nodeType==4  &&nodeTypeStatus==2 )
 *   患者ID、患者、阶段、创建时间、更新时间、预警等级、操作(预警处理)
 * 预警处理   已完成  ( nodeType==4  &&nodeTypeStatus==3 )
 *   患者ID、患者、阶段、创建时间、完成时间、预警等级、操作(预警处理)
 * 
 */
```