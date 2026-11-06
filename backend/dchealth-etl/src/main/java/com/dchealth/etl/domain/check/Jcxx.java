package com.dchealth.etl.domain.check;

import com.dchealth.etl.domain.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;

/**
 * 检查信息表(Jcxx)实体类
 *
 * @author makejava
 * @since 2023-03-22 17:44:07
 */
public class Jcxx extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 456486954470229992L;
    /**
     * studyId
     */
    private String id;
    /**
     * 身份证号（考试报名号）
     */
    private String sfzh;
    /**
     * 体检VIP
     */
    private String vipid;
    /**
     * 姓    名
     */
    private String xm;
    /**
     * 性    别
     */
    private String xb;
    /**
     * 出生年月
     */
    private String csny;
    /**
     * 婚    否
     */
    private String hf;
    /**
     * 民族
     */
    private String mz;
    /**
     * 通信地址
     */
    private String txdz;
    /**
     * 联系电话
     */
    private String lxdh;
    /**
     * 文化程度
     */
    private String whcd;
    /**
     * 职业
     */
    private String zy;
    /**
     * 所属省市
     */
    private String ssss;
    /**
     * 所属区县
     */
    private String ssqx;
    /**
     * 进厂(毕业)日期
     */
    private String jcrq;
    /**
     * 建档日期
     */
    private String jdrq;
    /**
     * 单位(毕业学校)代码
     */
    private String dwdm;
    /**
     * 单位分组代码
     */
    private String dwfzdm;
    /**
     * 岗位代码
     */
    private String gwdm;
    /**
     * 工种代码
     */
    private String gzdm;
    /**
     * 照    片
     */
    private String photo;
    /**
     * 登记项目
     */
    private String djxm;
    /**
     * 新登记项目
     */
    private String newdjxm;
    /**
     * 检查项目
     */
    private String jcxm;
    /**
     * 已查项目
     */
    private String ycxm;
    /**
     * 拒查项目
     */
    private String wcxm;
    /**
     * 未收金额
     */
    private BigDecimal djje;
    /**
     * 应收金额
     */
    private BigDecimal je;
    /**
     * 实收金额
     */
    private BigDecimal ssje;
    /**
     * 已收现金
     */
    private BigDecimal xjje;
    /**
     * 卡
     */
    private String card;
    /**
     * 工    号
     */
    private String gh;
    /**
     * 登记医生
     */
    private String djys;
    /**
     * 化验项目
     */
    private String hyxm;
    /**
     * PACS项目
     */
    private String pacsxm;
    /**
     * 检查类别
     */
    private String jclb;
    /**
     * 组合项目代码
     */
    private String zhxmdm;
    /**
     * 正在出结论的医生
     */
    private String jlys;
    /**
     * 申请医生
     */
    private String sqys;
    /**
     * 收费标记
     */
    private String sfbj;
    /**
     * 已收费标记
     */
    private String ysfbj;
    /**
     * 登记次数
     */
    private Integer djcount;
    /**
     * 收费医生
     */
    private String sfys;
    /**
     * 收费方式1金额
     */
    private BigDecimal xjje1;
    /**
     * 定义第二类收费方式
     */
    private BigDecimal xjje2;
    /**
     * 定义第三类收费方式
     */
    private BigDecimal xjje3;
    /**
     * 定义第四类收费方式
     */
    private BigDecimal xjje4;
    /**
     * 定义第五类收费方式
     */
    private BigDecimal xjje5;
    /**
     * 定义第六类收费方式
     */
    private BigDecimal xjje6;
    /**
     * 定义第七类收费方式
     */
    private BigDecimal xjje7;
    /**
     * 定义第八类收费方式
     */
    private BigDecimal xjje8;
    /**
     * 定义第九类收费方式
     */
    private BigDecimal xjje9;
    
    private BigDecimal xjje10;
    
    private BigDecimal xjje11;
    /**
     * 本此收费的应收金额
     */
    private String nowje;
    /**
     * 本此收费的实收金额
     */
    private BigDecimal nowssje;
    /**
     * 检查日期
     */
    private Date cyrq;
    /**
     * 打印次数
     */
    private Integer printcount;
    /**
     * 打印日期
     */
    private String printdate;
    /**
     * 打印人
     */
    private String printys;
    /**
     * 是否上传
     */
    private String ifupdown;
    /**
     * 上传时间
     */
    private String updowntime;
    /**
     * 折口率
     */
    private Float zkl;
    /**
     * 未检科室
     */
    private String wcks;
    /**
     * 口令
     */
    private String password;
    /**
     * 关注科室
     */
    private String gzks;
    /**
     * 核收报告日期
     */
    private String hsdate;
    /**
     * 核收人
     */
    private String hsys;
    /**
     * 国籍
     */
    private String gb;
    /**
     * Email
     */
    private String email;
    /**
     * 出生地
     */
    private String borthaddress;
    /**
     * 个人电脑号
     */
    private String grdnh;
    /**
     * 医保体检序号
     */
    private String insureseriesid;
    /**
     * 操作员ID
     */
    private String operaid;
    /**
     * 就餐时间
     */
    private String etdate;
    /**
     * 报到日期
     */
    private String ckdate;
    /**
     * 回收磁卡标记
     */
    private String rcardflag;
    /**
     * 回收日期
     */
    private String rcarddate;
    /**
     * 病历号
     */
    private String medicalNo;
    /**
     * 住院号
     */
    private String hosptialNo;
    /**
     * 门诊号
     */
    private String clinicNo;
    /**
     * 查体事项
     */
    private String notices;
    /**
     * 单位结算标志
     */
    private String countFlag;
    /**
     * 单位结算时间
     */
    private String countFlagDate;
    /**
     * 结算状态
     */
    private String countStatus;
    /**
     * HIS收费类别
     */
    private String tjfb;
    /**
     * HIS退费标记
     */
    private String histfbj;
    /**
     * HIS团体结账账号
     */
    private String hisgroupsfid;
    /**
     * 特殊人员
     */
    private String specialflag;
    /**
     * 健康证打印日期
     */
    private Date hdyrq;
    /**
     * 健康证打印人
     */
    private String hdyys;
    /**
     * 体检分部代码
     */
    private String deptcode;
    /**
     * 排检人群类型
     */
    private String ctype;
    /**
     * 人员类型编码
     */
    private String rylxbh;
    /**
     * 预检时间
     */
    private String yyrq;
    /**
     * 订单号
     */
    private String orderid;
    /**
     * 支付方式
     */
    private String paymode;
    /**
     * 互联网客户
     */
    private String ifweixin;
    
    private String lastmodiys;
    
    private String lastmodidate;
    
    private String pntbarcode;
    
    private String pntbarcodetime;
    
    private String recall;
    
    private String xmsx;
    
    private String queueid;
    
    private String contractId;
    
    private String weixinorderid;
    
    private String queuedate;
    
    private String queueinfo;
    
    private Integer ifsms;
    
    private String tjxmtypemc;
    
    private String fcid;
    
    private String djysdm;
    
    private Integer sfysdm;
    
    private String sqysdm;
    
    private String yyrq1;
    
    private String yyrqstr;
    
    private String ifsend;
    
    private String rylb;
    
    private String notices1;
    
    private String jjlxdh;
    
    private String ifyy;
    
    private String lqway;
    
    private String plevel;
    
    private String jlpatientbz;
    
    private String tcje;
    
    private String vipflag;
    
    private String isPqzt;
    
    private String fptitle;
    
    private String lqfs;
    
    private String queuearea;
    
    private String tzdprinter;
    
    private String glfzdm;
    
    private String ifems;
    
    private String idtype;
    
    private String printflag;
    
    private String yyrq2;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSfzh() {
        return sfzh;
    }

    public void setSfzh(String sfzh) {
        this.sfzh = sfzh;
    }

    public String getVipid() {
        return vipid;
    }

    public void setVipid(String vipid) {
        this.vipid = vipid;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getXb() {
        return xb;
    }

    public void setXb(String xb) {
        this.xb = xb;
    }

    public String getCsny() {
        return csny;
    }

    public void setCsny(String csny) {
        this.csny = csny;
    }

    public String getHf() {
        return hf;
    }

    public void setHf(String hf) {
        this.hf = hf;
    }

    public String getMz() {
        return mz;
    }

    public void setMz(String mz) {
        this.mz = mz;
    }

    public String getTxdz() {
        return txdz;
    }

    public void setTxdz(String txdz) {
        this.txdz = txdz;
    }

    public String getLxdh() {
        return lxdh;
    }

    public void setLxdh(String lxdh) {
        this.lxdh = lxdh;
    }

    public String getWhcd() {
        return whcd;
    }

    public void setWhcd(String whcd) {
        this.whcd = whcd;
    }

    public String getZy() {
        return zy;
    }

    public void setZy(String zy) {
        this.zy = zy;
    }

    public String getSsss() {
        return ssss;
    }

    public void setSsss(String ssss) {
        this.ssss = ssss;
    }

    public String getSsqx() {
        return ssqx;
    }

    public void setSsqx(String ssqx) {
        this.ssqx = ssqx;
    }

    public String getJcrq() {
        return jcrq;
    }

    public void setJcrq(String jcrq) {
        this.jcrq = jcrq;
    }

    public String getJdrq() {
        return jdrq;
    }

    public void setJdrq(String jdrq) {
        this.jdrq = jdrq;
    }

    public String getDwdm() {
        return dwdm;
    }

    public void setDwdm(String dwdm) {
        this.dwdm = dwdm;
    }

    public String getDwfzdm() {
        return dwfzdm;
    }

    public void setDwfzdm(String dwfzdm) {
        this.dwfzdm = dwfzdm;
    }

    public String getGwdm() {
        return gwdm;
    }

    public void setGwdm(String gwdm) {
        this.gwdm = gwdm;
    }

    public String getGzdm() {
        return gzdm;
    }

    public void setGzdm(String gzdm) {
        this.gzdm = gzdm;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDjxm() {
        return djxm;
    }

    public void setDjxm(String djxm) {
        this.djxm = djxm;
    }

    public String getNewdjxm() {
        return newdjxm;
    }

    public void setNewdjxm(String newdjxm) {
        this.newdjxm = newdjxm;
    }

    public String getJcxm() {
        return jcxm;
    }

    public void setJcxm(String jcxm) {
        this.jcxm = jcxm;
    }

    public String getYcxm() {
        return ycxm;
    }

    public void setYcxm(String ycxm) {
        this.ycxm = ycxm;
    }

    public String getWcxm() {
        return wcxm;
    }

    public void setWcxm(String wcxm) {
        this.wcxm = wcxm;
    }

    public BigDecimal getDjje() {
        return djje;
    }

    public void setDjje(BigDecimal djje) {
        this.djje = djje;
    }

    public BigDecimal getJe() {
        return je;
    }

    public void setJe(BigDecimal je) {
        this.je = je;
    }

    public BigDecimal getSsje() {
        return ssje;
    }

    public void setSsje(BigDecimal ssje) {
        this.ssje = ssje;
    }

    public BigDecimal getXjje() {
        return xjje;
    }

    public void setXjje(BigDecimal xjje) {
        this.xjje = xjje;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getGh() {
        return gh;
    }

    public void setGh(String gh) {
        this.gh = gh;
    }

    public String getDjys() {
        return djys;
    }

    public void setDjys(String djys) {
        this.djys = djys;
    }

    public String getHyxm() {
        return hyxm;
    }

    public void setHyxm(String hyxm) {
        this.hyxm = hyxm;
    }

    public String getPacsxm() {
        return pacsxm;
    }

    public void setPacsxm(String pacsxm) {
        this.pacsxm = pacsxm;
    }

    public String getJclb() {
        return jclb;
    }

    public void setJclb(String jclb) {
        this.jclb = jclb;
    }

    public String getZhxmdm() {
        return zhxmdm;
    }

    public void setZhxmdm(String zhxmdm) {
        this.zhxmdm = zhxmdm;
    }

    public String getJlys() {
        return jlys;
    }

    public void setJlys(String jlys) {
        this.jlys = jlys;
    }

    public String getSqys() {
        return sqys;
    }

    public void setSqys(String sqys) {
        this.sqys = sqys;
    }

    public String getSfbj() {
        return sfbj;
    }

    public void setSfbj(String sfbj) {
        this.sfbj = sfbj;
    }

    public String getYsfbj() {
        return ysfbj;
    }

    public void setYsfbj(String ysfbj) {
        this.ysfbj = ysfbj;
    }

    public Integer getDjcount() {
        return djcount;
    }

    public void setDjcount(Integer djcount) {
        this.djcount = djcount;
    }

    public String getSfys() {
        return sfys;
    }

    public void setSfys(String sfys) {
        this.sfys = sfys;
    }

    public BigDecimal getXjje1() {
        return xjje1;
    }

    public void setXjje1(BigDecimal xjje1) {
        this.xjje1 = xjje1;
    }

    public BigDecimal getXjje2() {
        return xjje2;
    }

    public void setXjje2(BigDecimal xjje2) {
        this.xjje2 = xjje2;
    }

    public BigDecimal getXjje3() {
        return xjje3;
    }

    public void setXjje3(BigDecimal xjje3) {
        this.xjje3 = xjje3;
    }

    public BigDecimal getXjje4() {
        return xjje4;
    }

    public void setXjje4(BigDecimal xjje4) {
        this.xjje4 = xjje4;
    }

    public BigDecimal getXjje5() {
        return xjje5;
    }

    public void setXjje5(BigDecimal xjje5) {
        this.xjje5 = xjje5;
    }

    public BigDecimal getXjje6() {
        return xjje6;
    }

    public void setXjje6(BigDecimal xjje6) {
        this.xjje6 = xjje6;
    }

    public BigDecimal getXjje7() {
        return xjje7;
    }

    public void setXjje7(BigDecimal xjje7) {
        this.xjje7 = xjje7;
    }

    public BigDecimal getXjje8() {
        return xjje8;
    }

    public void setXjje8(BigDecimal xjje8) {
        this.xjje8 = xjje8;
    }

    public BigDecimal getXjje9() {
        return xjje9;
    }

    public void setXjje9(BigDecimal xjje9) {
        this.xjje9 = xjje9;
    }

    public BigDecimal getXjje10() {
        return xjje10;
    }

    public void setXjje10(BigDecimal xjje10) {
        this.xjje10 = xjje10;
    }

    public BigDecimal getXjje11() {
        return xjje11;
    }

    public void setXjje11(BigDecimal xjje11) {
        this.xjje11 = xjje11;
    }

    public String getNowje() {
        return nowje;
    }

    public void setNowje(String nowje) {
        this.nowje = nowje;
    }

    public BigDecimal getNowssje() {
        return nowssje;
    }

    public void setNowssje(BigDecimal nowssje) {
        this.nowssje = nowssje;
    }

    public Date getCyrq() {
        return cyrq;
    }

    public void setCyrq(Date cyrq) {
        this.cyrq = cyrq;
    }

    public Integer getPrintcount() {
        return printcount;
    }

    public void setPrintcount(Integer printcount) {
        this.printcount = printcount;
    }

    public String getPrintdate() {
        return printdate;
    }

    public void setPrintdate(String printdate) {
        this.printdate = printdate;
    }

    public String getPrintys() {
        return printys;
    }

    public void setPrintys(String printys) {
        this.printys = printys;
    }

    public String getIfupdown() {
        return ifupdown;
    }

    public void setIfupdown(String ifupdown) {
        this.ifupdown = ifupdown;
    }

    public String getUpdowntime() {
        return updowntime;
    }

    public void setUpdowntime(String updowntime) {
        this.updowntime = updowntime;
    }

    public Float getZkl() {
        return zkl;
    }

    public void setZkl(Float zkl) {
        this.zkl = zkl;
    }

    public String getWcks() {
        return wcks;
    }

    public void setWcks(String wcks) {
        this.wcks = wcks;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGzks() {
        return gzks;
    }

    public void setGzks(String gzks) {
        this.gzks = gzks;
    }

    public String getHsdate() {
        return hsdate;
    }

    public void setHsdate(String hsdate) {
        this.hsdate = hsdate;
    }

    public String getHsys() {
        return hsys;
    }

    public void setHsys(String hsys) {
        this.hsys = hsys;
    }

    public String getGb() {
        return gb;
    }

    public void setGb(String gb) {
        this.gb = gb;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBorthaddress() {
        return borthaddress;
    }

    public void setBorthaddress(String borthaddress) {
        this.borthaddress = borthaddress;
    }

    public String getGrdnh() {
        return grdnh;
    }

    public void setGrdnh(String grdnh) {
        this.grdnh = grdnh;
    }

    public String getInsureseriesid() {
        return insureseriesid;
    }

    public void setInsureseriesid(String insureseriesid) {
        this.insureseriesid = insureseriesid;
    }

    public String getOperaid() {
        return operaid;
    }

    public void setOperaid(String operaid) {
        this.operaid = operaid;
    }

    public String getEtdate() {
        return etdate;
    }

    public void setEtdate(String etdate) {
        this.etdate = etdate;
    }

    public String getCkdate() {
        return ckdate;
    }

    public void setCkdate(String ckdate) {
        this.ckdate = ckdate;
    }

    public String getRcardflag() {
        return rcardflag;
    }

    public void setRcardflag(String rcardflag) {
        this.rcardflag = rcardflag;
    }

    public String getRcarddate() {
        return rcarddate;
    }

    public void setRcarddate(String rcarddate) {
        this.rcarddate = rcarddate;
    }

    public String getMedicalNo() {
        return medicalNo;
    }

    public void setMedicalNo(String medicalNo) {
        this.medicalNo = medicalNo;
    }

    public String getHosptialNo() {
        return hosptialNo;
    }

    public void setHosptialNo(String hosptialNo) {
        this.hosptialNo = hosptialNo;
    }

    public String getClinicNo() {
        return clinicNo;
    }

    public void setClinicNo(String clinicNo) {
        this.clinicNo = clinicNo;
    }

    public String getNotices() {
        return notices;
    }

    public void setNotices(String notices) {
        this.notices = notices;
    }

    public String getCountFlag() {
        return countFlag;
    }

    public void setCountFlag(String countFlag) {
        this.countFlag = countFlag;
    }

    public String getCountFlagDate() {
        return countFlagDate;
    }

    public void setCountFlagDate(String countFlagDate) {
        this.countFlagDate = countFlagDate;
    }

    public String getCountStatus() {
        return countStatus;
    }

    public void setCountStatus(String countStatus) {
        this.countStatus = countStatus;
    }

    public String getTjfb() {
        return tjfb;
    }

    public void setTjfb(String tjfb) {
        this.tjfb = tjfb;
    }

    public String getHistfbj() {
        return histfbj;
    }

    public void setHistfbj(String histfbj) {
        this.histfbj = histfbj;
    }

    public String getHisgroupsfid() {
        return hisgroupsfid;
    }

    public void setHisgroupsfid(String hisgroupsfid) {
        this.hisgroupsfid = hisgroupsfid;
    }

    public String getSpecialflag() {
        return specialflag;
    }

    public void setSpecialflag(String specialflag) {
        this.specialflag = specialflag;
    }

    public Date getHdyrq() {
        return hdyrq;
    }

    public void setHdyrq(Date hdyrq) {
        this.hdyrq = hdyrq;
    }

    public String getHdyys() {
        return hdyys;
    }

    public void setHdyys(String hdyys) {
        this.hdyys = hdyys;
    }

    public String getDeptcode() {
        return deptcode;
    }

    public void setDeptcode(String deptcode) {
        this.deptcode = deptcode;
    }

    public String getCtype() {
        return ctype;
    }

    public void setCtype(String ctype) {
        this.ctype = ctype;
    }

    public String getRylxbh() {
        return rylxbh;
    }

    public void setRylxbh(String rylxbh) {
        this.rylxbh = rylxbh;
    }

    public String getYyrq() {
        return yyrq;
    }

    public void setYyrq(String yyrq) {
        this.yyrq = yyrq;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getPaymode() {
        return paymode;
    }

    public void setPaymode(String paymode) {
        this.paymode = paymode;
    }

    public String getIfweixin() {
        return ifweixin;
    }

    public void setIfweixin(String ifweixin) {
        this.ifweixin = ifweixin;
    }

    public String getLastmodiys() {
        return lastmodiys;
    }

    public void setLastmodiys(String lastmodiys) {
        this.lastmodiys = lastmodiys;
    }

    public String getLastmodidate() {
        return lastmodidate;
    }

    public void setLastmodidate(String lastmodidate) {
        this.lastmodidate = lastmodidate;
    }

    public String getPntbarcode() {
        return pntbarcode;
    }

    public void setPntbarcode(String pntbarcode) {
        this.pntbarcode = pntbarcode;
    }

    public String getPntbarcodetime() {
        return pntbarcodetime;
    }

    public void setPntbarcodetime(String pntbarcodetime) {
        this.pntbarcodetime = pntbarcodetime;
    }

    public String getRecall() {
        return recall;
    }

    public void setRecall(String recall) {
        this.recall = recall;
    }

    public String getXmsx() {
        return xmsx;
    }

    public void setXmsx(String xmsx) {
        this.xmsx = xmsx;
    }

    public String getQueueid() {
        return queueid;
    }

    public void setQueueid(String queueid) {
        this.queueid = queueid;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getWeixinorderid() {
        return weixinorderid;
    }

    public void setWeixinorderid(String weixinorderid) {
        this.weixinorderid = weixinorderid;
    }

    public String getQueuedate() {
        return queuedate;
    }

    public void setQueuedate(String queuedate) {
        this.queuedate = queuedate;
    }

    public String getQueueinfo() {
        return queueinfo;
    }

    public void setQueueinfo(String queueinfo) {
        this.queueinfo = queueinfo;
    }

    public Integer getIfsms() {
        return ifsms;
    }

    public void setIfsms(Integer ifsms) {
        this.ifsms = ifsms;
    }

    public String getTjxmtypemc() {
        return tjxmtypemc;
    }

    public void setTjxmtypemc(String tjxmtypemc) {
        this.tjxmtypemc = tjxmtypemc;
    }

    public String getFcid() {
        return fcid;
    }

    public void setFcid(String fcid) {
        this.fcid = fcid;
    }

    public String getDjysdm() {
        return djysdm;
    }

    public void setDjysdm(String djysdm) {
        this.djysdm = djysdm;
    }

    public Integer getSfysdm() {
        return sfysdm;
    }

    public void setSfysdm(Integer sfysdm) {
        this.sfysdm = sfysdm;
    }

    public String getSqysdm() {
        return sqysdm;
    }

    public void setSqysdm(String sqysdm) {
        this.sqysdm = sqysdm;
    }

    public String getYyrq1() {
        return yyrq1;
    }

    public void setYyrq1(String yyrq1) {
        this.yyrq1 = yyrq1;
    }

    public String getYyrqstr() {
        return yyrqstr;
    }

    public void setYyrqstr(String yyrqstr) {
        this.yyrqstr = yyrqstr;
    }

    public String getIfsend() {
        return ifsend;
    }

    public void setIfsend(String ifsend) {
        this.ifsend = ifsend;
    }

    public String getRylb() {
        return rylb;
    }

    public void setRylb(String rylb) {
        this.rylb = rylb;
    }

    public String getNotices1() {
        return notices1;
    }

    public void setNotices1(String notices1) {
        this.notices1 = notices1;
    }

    public String getJjlxdh() {
        return jjlxdh;
    }

    public void setJjlxdh(String jjlxdh) {
        this.jjlxdh = jjlxdh;
    }

    public String getIfyy() {
        return ifyy;
    }

    public void setIfyy(String ifyy) {
        this.ifyy = ifyy;
    }

    public String getLqway() {
        return lqway;
    }

    public void setLqway(String lqway) {
        this.lqway = lqway;
    }

    public String getPlevel() {
        return plevel;
    }

    public void setPlevel(String plevel) {
        this.plevel = plevel;
    }

    public String getJlpatientbz() {
        return jlpatientbz;
    }

    public void setJlpatientbz(String jlpatientbz) {
        this.jlpatientbz = jlpatientbz;
    }

    public String getTcje() {
        return tcje;
    }

    public void setTcje(String tcje) {
        this.tcje = tcje;
    }

    public String getVipflag() {
        return vipflag;
    }

    public void setVipflag(String vipflag) {
        this.vipflag = vipflag;
    }

    public String getIsPqzt() {
        return isPqzt;
    }

    public void setIsPqzt(String isPqzt) {
        this.isPqzt = isPqzt;
    }

    public String getFptitle() {
        return fptitle;
    }

    public void setFptitle(String fptitle) {
        this.fptitle = fptitle;
    }

    public String getLqfs() {
        return lqfs;
    }

    public void setLqfs(String lqfs) {
        this.lqfs = lqfs;
    }

    public String getQueuearea() {
        return queuearea;
    }

    public void setQueuearea(String queuearea) {
        this.queuearea = queuearea;
    }

    public String getTzdprinter() {
        return tzdprinter;
    }

    public void setTzdprinter(String tzdprinter) {
        this.tzdprinter = tzdprinter;
    }

    public String getGlfzdm() {
        return glfzdm;
    }

    public void setGlfzdm(String glfzdm) {
        this.glfzdm = glfzdm;
    }

    public String getIfems() {
        return ifems;
    }

    public void setIfems(String ifems) {
        this.ifems = ifems;
    }

    public String getIdtype() {
        return idtype;
    }

    public void setIdtype(String idtype) {
        this.idtype = idtype;
    }

    public String getPrintflag() {
        return printflag;
    }

    public void setPrintflag(String printflag) {
        this.printflag = printflag;
    }

    public String getYyrq2() {
        return yyrq2;
    }

    public void setYyrq2(String yyrq2) {
        this.yyrq2 = yyrq2;
    }

}

