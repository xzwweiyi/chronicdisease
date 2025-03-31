package com.dchealth.cro.medical.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dchealth.cro.medical.domain.WarnDoctorPatient;

@Repository
public interface WarnDoctorPatientMapper {

    List<WarnDoctorPatient> selectWarnDoctorPatient(WarnDoctorPatient warnDoctorPatient);

    int insert( WarnDoctorPatient warnDoctorPatient);
    int  updateWarnDockerPatient( WarnDoctorPatient warnDoctorPatient);
    int update(WarnDoctorPatient warnDoctorPatient);
}
