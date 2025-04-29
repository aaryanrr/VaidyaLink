package com.mustard.vaidyalink.services;

import com.mustard.vaidyalink.entities.InstitutionAccessData;
import com.mustard.vaidyalink.entities.User;
import com.mustard.vaidyalink.repositories.InstitutionAccessDataRepository;
import com.mustard.vaidyalink.repositories.UserRepository;
import com.mustard.vaidyalink.utils.EncryptionUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
public class SyncService {

    private final InstitutionAccessDataRepository institutionAccessDataRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SyncService(InstitutionAccessDataRepository institutionAccessDataRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.institutionAccessDataRepository = institutionAccessDataRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void syncApproved(String encodedAccessRequestId, String encodedAccessKey, String encodedPassword) {
        String accessRequestId = new String(Base64.getDecoder().decode(encodedAccessRequestId));
        String accessKey = new String(Base64.getDecoder().decode(encodedAccessKey));
        String password = new String(Base64.getDecoder().decode(encodedPassword));
        UUID reqId = UUID.fromString(accessRequestId);

        InstitutionAccessData data = institutionAccessDataRepository.findAllByAccessRequestId(reqId)
                .stream().findFirst().orElseThrow(() -> new IllegalArgumentException("No data found for this access request."));

        Optional<User> userOpt = userRepository.findByEmail(EncryptionUtil.encryptDecryptString("decrypt", data.getEmail(), accessKey));
        if (userOpt.isEmpty()) throw new IllegalArgumentException("User not found.");

        User user = userOpt.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Incorrect password.");
        }

        try {
            user.setPhoneNumber(EncryptionUtil.encryptDecryptString("encrypt", EncryptionUtil.encryptDecryptString("decrypt", data.getPhoneNumber(), accessKey), password));
            user.setAddress(EncryptionUtil.encryptDecryptString("encrypt", EncryptionUtil.encryptDecryptString("decrypt", data.getAddress(), accessKey), password));
            user.setBloodGroup(EncryptionUtil.encryptDecryptString("encrypt", EncryptionUtil.encryptDecryptString("decrypt", data.getBloodGroup(), accessKey), password));
            user.setEmergencyContact(EncryptionUtil.encryptDecryptString("encrypt", EncryptionUtil.encryptDecryptString("decrypt", data.getEmergencyContact(), accessKey), password));
            user.setAllergies(EncryptionUtil.encryptDecryptString("encrypt", EncryptionUtil.encryptDecryptString("decrypt", data.getAllergies(), accessKey), password));
            user.setHeightCm(EncryptionUtil.encryptDecryptDouble("encrypt", EncryptionUtil.encryptDecryptDouble("decrypt", data.getHeightCm(), accessKey), password));
            user.setWeightKg(EncryptionUtil.encryptDecryptDouble("encrypt", EncryptionUtil.encryptDecryptDouble("decrypt", data.getWeightKg(), accessKey), password));
            userRepository.save(user);
        } catch (Exception e) {
            throw new IllegalArgumentException("Access Key is invalid or data could not be decrypted.");
        }
    }
}
