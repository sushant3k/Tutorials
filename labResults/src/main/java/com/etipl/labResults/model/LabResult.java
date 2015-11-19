package com.etipl.labResults.model;

import com.etipl.labResults.nosql.framework.annotations.HColumnQualifier;
import com.etipl.labResults.nosql.framework.annotations.HRowIdentifier;
import com.etipl.labResults.nosql.framework.annotations.HRowIdentifierGenerator;
import com.etipl.labResults.nosql.framework.annotations.HTableQualifier;

/**
 * 
 * @author Sushant
 *
 */

@HTableQualifier(tableName="labResult")
public class LabResult {

	@HRowIdentifier(identifier=HRowIdentifierGenerator.CUSTOM)
	private String rowKey;
	
	/**
	 * Start configuration for the Patient Lab Order Info
	 */
	@HColumnQualifier(columnFamily="patLabOrderInfo")
	private String orderSource;
	
	@HColumnQualifier(columnFamily="patLabOrderInfo")
	private String orderType;
	
	@HColumnQualifier(columnFamily="patLabOrderInfo")
	private String orderNo;
	
	@HColumnQualifier(columnFamily="patLabOrderInfo")
	private String fillerOrderNo;
	
	@HColumnQualifier(columnFamily="patLabOrderInfo")
	private String placerGroupNo;
	
	@HColumnQualifier(columnFamily="patLabOrderInfo")
	private String orderingProviderId;
	
	@HColumnQualifier(columnFamily="patLabOrderInfo")
	private String orderHL7Msg;
	
	@HColumnQualifier(columnFamily="patLabOrderInfo")
	private String createdOn;
	
	@HColumnQualifier(columnFamily="patLabOrderInfo")
	private String createdBy;
	
	@HColumnQualifier(columnFamily="patLabOrderInfo")
	private String lastModifiedDate;
	
	@HColumnQualifier(columnFamily="patLabOrderInfo")
	private String modifiedBy;
	
	@HColumnQualifier(columnFamily="patLabOrderInfo")
	private String orderResultStatus;
	
	@HColumnQualifier(columnFamily="patLabOrderInfo")
	private String collectionDateTime;
	
	@HColumnQualifier(columnFamily="patLabOrderInfo")
	private String orderReceivedDateTime;
	
	@HColumnQualifier(columnFamily="patLabOrderInfo")
	private String testName;
	
	@HColumnQualifier(columnFamily="patLabOrderInfo")
	private String specimen;
	
	@HColumnQualifier(columnFamily="patLabOrderInfo")
	private String parentId;
	
	@HColumnQualifier(columnFamily="patLabOrderInfo")
	private String analyteSequenceId;
	
	@HColumnQualifier(columnFamily="patLabOrderInfo")
	private String orderNotes;
	
	@HColumnQualifier(columnFamily="patLabOrderInfo")
	private String orderDateTime;
	
	@HColumnQualifier(columnFamily="patLabOrderInfo")
	private String orderingFacility;
	
	@HColumnQualifier(columnFamily="patLabOrderInfo")
	private String priority;
	
	/**
	 * End Patient Lab Order Info
	 */
	
	/**
	 * Start Patient Column Family.
	 */
	@HColumnQualifier(columnFamily="patient")
	private String patientId;
	
	@HColumnQualifier(columnFamily="patient")
	private String patientDetails;
	
	
	/**
	 * Start Patient Lab Result Info column Family
	 */
	
	@HColumnQualifier(columnFamily="patLabResultInfo")
	private String resultDateTime;
	
	@HColumnQualifier(columnFamily="patLabResultInfo")
	private String resultCopyTo;
	
	@HColumnQualifier(columnFamily="patLabResultInfo")
	private String specimenActionCode;
	
	@HColumnQualifier(columnFamily="patLabResultInfo")
	private String observationNotes;
	
	@HColumnQualifier(columnFamily="patLabResultInfo")
	private String clinicalInfo;
	
	@HColumnQualifier(columnFamily="patLabResultInfo")
	private String specimenCondition;
	
	@HColumnQualifier(columnFamily="patLabResultInfo")
	private String specimenRejectedReason;
	
	@HColumnQualifier(columnFamily="patLabResultInfo")
	private String timingStart;
	
	@HColumnQualifier(columnFamily="patLabResultInfo")
	private String timingEnd;
	
	@HColumnQualifier(columnFamily="patLabResultInfo")
	private String labName;
	
	@HColumnQualifier(columnFamily="patLabResultInfo")
	private String labDirectorId;
	
	@HColumnQualifier(columnFamily="patLabResultInfo")
	private String resultHL7Registry;
	
	@HColumnQualifier(columnFamily="patLabResultInfo")
	private String resultHL7VistA;
	
	@HColumnQualifier(columnFamily="patLabResultInfo")
	private String receivingApplication;
	
	@HColumnQualifier(columnFamily="patLabResultInfo")
	private String receivingFacility;
	
	@HColumnQualifier(columnName="createdOn",columnFamily="patLabResultInfo")
	private String resultCreatedOn;
	
	@HColumnQualifier(columnName="createdBy",columnFamily="patLabResultInfo")
	private String resultCreatedBy;
	
	@HColumnQualifier(columnName="lastModifiedDate",columnFamily="patLabResultInfo")
	private String lmodifiedDate;
	
	@HColumnQualifier(columnName="lastModifiedDate",columnFamily="patLabResultInfo")
	private String lastModifiedBy;
	
	public LabResult()
	{
		
	}

	public String getRowKey() {
		return rowKey;
	}

	public void setRowKey(String rowKey) {
		this.rowKey = rowKey;
	}

	public String getOrderSource() {
		return orderSource;
	}

	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getFillerOrderNo() {
		return fillerOrderNo;
	}

	public void setFillerOrderNo(String fillerOrderNo) {
		this.fillerOrderNo = fillerOrderNo;
	}

	public String getPlacerGroupNo() {
		return placerGroupNo;
	}

	public void setPlacerGroupNo(String placerGroupNo) {
		this.placerGroupNo = placerGroupNo;
	}

	public String getOrderingProviderId() {
		return orderingProviderId;
	}

	public void setOrderingProviderId(String orderingProviderId) {
		this.orderingProviderId = orderingProviderId;
	}

	public String getOrderHL7Msg() {
		return orderHL7Msg;
	}

	public void setOrderHL7Msg(String orderHL7Msg) {
		this.orderHL7Msg = orderHL7Msg;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getOrderResultStatus() {
		return orderResultStatus;
	}

	public void setOrderResultStatus(String orderResultStatus) {
		this.orderResultStatus = orderResultStatus;
	}

	public String getCollectionDateTime() {
		return collectionDateTime;
	}

	public void setCollectionDateTime(String collectionDateTime) {
		this.collectionDateTime = collectionDateTime;
	}

	public String getOrderReceivedDateTime() {
		return orderReceivedDateTime;
	}

	public void setOrderReceivedDateTime(String orderReceivedDateTime) {
		this.orderReceivedDateTime = orderReceivedDateTime;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public String getSpecimen() {
		return specimen;
	}

	public void setSpecimen(String specimen) {
		this.specimen = specimen;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getAnalyteSequenceId() {
		return analyteSequenceId;
	}

	public void setAnalyteSequenceId(String analyteSequenceId) {
		this.analyteSequenceId = analyteSequenceId;
	}

	public String getOrderNotes() {
		return orderNotes;
	}

	public void setOrderNotes(String orderNotes) {
		this.orderNotes = orderNotes;
	}

	public String getOrderDateTime() {
		return orderDateTime;
	}

	public void setOrderDateTime(String orderDateTime) {
		this.orderDateTime = orderDateTime;
	}

	public String getOrderingFacility() {
		return orderingFacility;
	}

	public void setOrderingFacility(String orderingFacility) {
		this.orderingFacility = orderingFacility;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getPatientDetails() {
		return patientDetails;
	}

	public void setPatientDetails(String patientDetails) {
		this.patientDetails = patientDetails;
	}

	public String getResultDateTime() {
		return resultDateTime;
	}

	public void setResultDateTime(String resultDateTime) {
		this.resultDateTime = resultDateTime;
	}

	public String getResultCopyTo() {
		return resultCopyTo;
	}

	public void setResultCopyTo(String resultCopyTo) {
		this.resultCopyTo = resultCopyTo;
	}

	public String getSpecimenActionCode() {
		return specimenActionCode;
	}

	public void setSpecimenActionCode(String specimenActionCode) {
		this.specimenActionCode = specimenActionCode;
	}

	public String getObservationNotes() {
		return observationNotes;
	}

	public void setObservationNotes(String observationNotes) {
		this.observationNotes = observationNotes;
	}

	public String getClinicalInfo() {
		return clinicalInfo;
	}

	public void setClinicalInfo(String clinicalInfo) {
		this.clinicalInfo = clinicalInfo;
	}

	public String getSpecimenCondition() {
		return specimenCondition;
	}

	public void setSpecimenCondition(String specimenCondition) {
		this.specimenCondition = specimenCondition;
	}

	public String getSpecimenRejectedReason() {
		return specimenRejectedReason;
	}

	public void setSpecimenRejectedReason(String specimenRejectedReason) {
		this.specimenRejectedReason = specimenRejectedReason;
	}

	public String getTimingStart() {
		return timingStart;
	}

	public void setTimingStart(String timingStart) {
		this.timingStart = timingStart;
	}

	public String getTimingEnd() {
		return timingEnd;
	}

	public void setTimingEnd(String timingEnd) {
		this.timingEnd = timingEnd;
	}

	public String getLabName() {
		return labName;
	}

	public void setLabName(String labName) {
		this.labName = labName;
	}

	public String getLabDirectorId() {
		return labDirectorId;
	}

	public void setLabDirectorId(String labDirectorId) {
		this.labDirectorId = labDirectorId;
	}

	public String getResultHL7Registry() {
		return resultHL7Registry;
	}

	public void setResultHL7Registry(String resultHL7Registry) {
		this.resultHL7Registry = resultHL7Registry;
	}

	public String getResultHL7VistA() {
		return resultHL7VistA;
	}

	public void setResultHL7VistA(String resultHL7VistA) {
		this.resultHL7VistA = resultHL7VistA;
	}

	public String getReceivingApplication() {
		return receivingApplication;
	}

	public void setReceivingApplication(String receivingApplication) {
		this.receivingApplication = receivingApplication;
	}

	public String getReceivingFacility() {
		return receivingFacility;
	}

	public void setReceivingFacility(String receivingFacility) {
		this.receivingFacility = receivingFacility;
	}

	public String getResultCreatedOn() {
		return resultCreatedOn;
	}

	public void setResultCreatedOn(String resultCreatedOn) {
		this.resultCreatedOn = resultCreatedOn;
	}

	public String getResultCreatedBy() {
		return resultCreatedBy;
	}

	public void setResultCreatedBy(String resultCreatedBy) {
		this.resultCreatedBy = resultCreatedBy;
	}

	public String getLmodifiedDate() {
		return lmodifiedDate;
	}

	public void setLmodifiedDate(String lmodifiedDate) {
		this.lmodifiedDate = lmodifiedDate;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	
	
}
