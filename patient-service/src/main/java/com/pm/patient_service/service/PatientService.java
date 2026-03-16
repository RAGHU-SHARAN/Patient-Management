package com.pm.patient_service.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pm.patient_service.dto.PatientRequestDTO;
import com.pm.patient_service.dto.PatientResponseDTO;
import com.pm.patient_service.exception.EmailAlreadyExistException;
import com.pm.patient_service.exception.PatientNotFoundException;
import com.pm.patient_service.mapper.PatientMapper;
import com.pm.patient_service.model.Patient;
import com.pm.patient_service.repository.PatientRepository;

@Service
public class PatientService {
	private PatientRepository patientRepository;

	public PatientService(PatientRepository patientRepository) {
		this.patientRepository = patientRepository;
	}

	public List<PatientResponseDTO> getPatients() {
		List<Patient> patients = patientRepository.findAll();

		// Mapping Patient to PatientResponseDTO
		List<PatientResponseDTO> patientResponseDTOs = patients.stream().map(patient -> PatientMapper.toDTO(patient))
				.toList();

		return patientResponseDTOs;
	}

	public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
		if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
			throw new EmailAlreadyExistException(
					"A patient with this Email " + "already exists " + patientRequestDTO.getEmail());
		}
		Patient newPatient = patientRepository.save(PatientMapper.toEntity(patientRequestDTO));
		return PatientMapper.toDTO(newPatient);
	}

	public PatientResponseDTO updatePatient(Long id, PatientRequestDTO patientRequestDTO) {

		Patient existingPatient = patientRepository.findById(id)
				.orElseThrow(() -> new PatientNotFoundException("Patient not found with ID: " + id));

		if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), id)) {
			throw new EmailAlreadyExistException(
					"A patient with this Email " + "already exists " + patientRequestDTO.getEmail());
		}

		existingPatient.setName(patientRequestDTO.getName());
		existingPatient.setEmail(patientRequestDTO.getEmail());
		existingPatient.setAddress(patientRequestDTO.getAddress());
		existingPatient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));

		Patient updatedPatient = patientRepository.save(existingPatient);
		return PatientMapper.toDTO(updatedPatient);
	}

	public void deletePatient(Long id) {
		if (!patientRepository.existsById(id)) {
			throw new PatientNotFoundException("Patient not found with ID: " + id);
		}
		patientRepository.deleteById(id);
	}

}
