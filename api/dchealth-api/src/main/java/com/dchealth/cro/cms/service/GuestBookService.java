package com.dchealth.cro.cms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dchealth.cro.cms.domain.CmsGuestImages;
import com.dchealth.cro.cms.domain.CmsGuestbook;
import com.dchealth.cro.cms.form.GuestBookForm;
import com.dchealth.cro.cms.mapper.CmsGuestImagesMapper;
import com.dchealth.cro.cms.mapper.CmsGuestbookMapper;
import com.dchealth.cro.common.base.CommonResult;
import com.dchealth.cro.common.enums.DeleteFlag;
import com.dchealth.cro.common.utils.DateUtils;

@Service
public class GuestBookService {
	@Autowired
	private CmsGuestbookMapper cmsGuestbookMapper;

	@Autowired
	private CmsGuestImagesMapper cmsGuestImagesMapper;

	@Transactional
	public CommonResult save(GuestBookForm guestBookForm) {
		CmsGuestbook guestbook = new CmsGuestbook();
		guestbook.setAccountId(guestBookForm.getAccountIds());
		guestbook.setContent(guestBookForm.getContent());
		guestbook.setCreateTime(DateUtils.getNowDate());
		guestbook.setDelFlag(DeleteFlag.OK.getCode());
		guestbook.setMobile(guestBookForm.getMobile());
		guestbook.setTenantId(guestBookForm.getTenantId());
		guestbook.setUserName(guestBookForm.getUserName());

		cmsGuestbookMapper.insertCmsGuestbook(guestbook);
		List<String> list = guestBookForm.getList();
		if (list != null && list.size() > 0) {
			for (String url : list) {
				CmsGuestImages cmsGuestImages = new CmsGuestImages();
				cmsGuestImages.setCreateTime(DateUtils.getNowDate());
				cmsGuestImages.setGuestbookId(guestbook.getId());
				cmsGuestImages.setImageUrl(url);
				cmsGuestImagesMapper.insertCmsGuestImages(cmsGuestImages);
			}
		}
		CommonResult commonResult = new CommonResult();
		commonResult.setResult(true);
		return commonResult;
	}
}
