package com.mustard.vaidyalink.contract;

import io.reactivex.Flowable;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/hyperledger-web3j/web3j/tree/main/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.6.3.
 */
@SuppressWarnings("rawtypes")
public class MedicalDataAccess extends Contract {
    public static final String BINARY = "6080604052348015600e575f5ffd5b5061059c8061001c5f395ff3fe608060405234801561000f575f5ffd5b5060043610610091575f3560e01c8063682697021161006457806368269702146101055780637ff4da5314610121578063986544621461013d578063d6aa90da14610159578063f0143bc91461017557610091565b80631db4c24c146100955780632ed618a4146100b15780633bed3541146100cd578063680d1ba8146100e9575b5f5ffd5b6100af60048036038101906100aa9190610455565b610191565b005b6100cb60048036038101906100c69190610455565b6101d4565b005b6100e760048036038101906100e29190610455565b610217565b005b61010360048036038101906100fe9190610455565b61025a565b005b61011f600480360381019061011a9190610455565b61029d565b005b61013b60048036038101906101369190610455565b6102e0565b005b61015760048036038101906101529190610455565b610323565b005b610173600480360381019061016e9190610455565b610366565b005b61018f600480360381019061018a9190610455565b6103a9565b005b7f7ecbd2f6b980f849cf85565fa623280e436a69785ec7699133858dfea836672c848484846040516101c6949392919061052d565b60405180910390a150505050565b7fd3e2d08262b3e953832b809909b3e06137ec1523f3bbf5c56c8716b235d1292384848484604051610209949392919061052d565b60405180910390a150505050565b7fb320c9d8d7c0eb1c15fc1c139a61cf3b8d4ef7b0f67c05a6983d7f1b47a61fc78484848460405161024c949392919061052d565b60405180910390a150505050565b7f93d2d3fac52abb908a53f7727f936a2bbf17ca1ba94dee6a398d90a0dd99d9c88484848460405161028f949392919061052d565b60405180910390a150505050565b7f5190aaea1de4c067884e2406c78a0f01c38a61b2733c5abcdf95d7a9aaf6b567848484846040516102d2949392919061052d565b60405180910390a150505050565b7f7c35fd9f39871318a78c033d2287e9f5ff6cda8740a18ea6e81d0b780801973e84848484604051610315949392919061052d565b60405180910390a150505050565b7f78477db79e1223fd64a63e7d9c13977d29ba6f7b30aaa81cf7d662ff4244b16884848484604051610358949392919061052d565b60405180910390a150505050565b7f33a7f01909036d0f551965df809bb67bc291a00e2f1c897782435829b84c2b458484848460405161039b949392919061052d565b60405180910390a150505050565b7f503cd303facddec2685e3fccc2788779427b02d100d380bc6f5ddb588d3d823f848484846040516103de949392919061052d565b60405180910390a150505050565b5f5ffd5b5f5ffd5b5f5ffd5b5f5ffd5b5f5ffd5b5f5f83601f840112610415576104146103f4565b5b8235905067ffffffffffffffff811115610432576104316103f8565b5b60208301915083600182028301111561044e5761044d6103fc565b5b9250929050565b5f5f5f5f6040858703121561046d5761046c6103ec565b5b5f85013567ffffffffffffffff81111561048a576104896103f0565b5b61049687828801610400565b9450945050602085013567ffffffffffffffff8111156104b9576104b86103f0565b5b6104c587828801610400565b925092505092959194509250565b5f82825260208201905092915050565b828183375f83830152505050565b5f601f19601f8301169050919050565b5f61050c83856104d3565b93506105198385846104e3565b610522836104f1565b840190509392505050565b5f6040820190508181035f830152610546818688610501565b9050818103602083015261055b818486610501565b90509594505050505056fea2646970667358221220003651933061fae9c8d3bddc3963d1396ee2e66f584b90e921cefa7d6369ae0c64736f6c634300081d0033";

    private static String librariesLinkedBinary;

    public static final String FUNC_LOGACCESSREQUESTAPPROVED = "logAccessRequestApproved";

    public static final String FUNC_LOGACCESSREQUESTCREATED = "logAccessRequestCreated";

    public static final String FUNC_LOGACCESSREVOKED = "logAccessRevoked";

    public static final String FUNC_LOGBASICDATAVIEWED = "logBasicDataViewed";

    public static final String FUNC_LOGDATAEDITEDFORUSER = "logDataEditedForUser";

    public static final String FUNC_LOGINSTITUTIONREGISTERED = "logInstitutionRegistered";

    public static final String FUNC_LOGUSERACCOUNTDELETED = "logUserAccountDeleted";

    public static final String FUNC_LOGUSERINVITED = "logUserInvited";

    public static final String FUNC_LOGUSERVIEWEDRECORDS = "logUserViewedRecords";

    public static final Event ACCESSREQUESTAPPROVED_EVENT = new Event("AccessRequestApproved",
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
            }, new TypeReference<Utf8String>() {
            }));
    ;

    public static final Event ACCESSREQUESTCREATED_EVENT = new Event("AccessRequestCreated",
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
            }, new TypeReference<Utf8String>() {
            }));
    ;

    public static final Event ACCESSREVOKED_EVENT = new Event("AccessRevoked",
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
            }, new TypeReference<Utf8String>() {
            }));
    ;

    public static final Event BASICDATAVIEWED_EVENT = new Event("BasicDataViewed",
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
            }, new TypeReference<Utf8String>() {
            }));
    ;

    public static final Event DATAEDITEDFORUSER_EVENT = new Event("DataEditedForUser",
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
            }, new TypeReference<Utf8String>() {
            }));
    ;

    public static final Event INSTITUTIONREGISTERED_EVENT = new Event("InstitutionRegistered",
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
            }, new TypeReference<Utf8String>() {
            }));
    ;

    public static final Event USERACCOUNTDELETED_EVENT = new Event("UserAccountDeleted",
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
            }, new TypeReference<Utf8String>() {
            }));
    ;

    public static final Event USERINVITED_EVENT = new Event("UserInvited",
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
            }, new TypeReference<Utf8String>() {
            }));
    ;

    public static final Event USERVIEWEDRECORDS_EVENT = new Event("UserViewedRecords",
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
            }, new TypeReference<Utf8String>() {
            }));
    ;

    @Deprecated
    protected MedicalDataAccess(String contractAddress, Web3j web3j, Credentials credentials,
                                BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected MedicalDataAccess(String contractAddress, Web3j web3j, Credentials credentials,
                                ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected MedicalDataAccess(String contractAddress, Web3j web3j,
                                TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected MedicalDataAccess(String contractAddress, Web3j web3j,
                                TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<AccessRequestApprovedEventResponse> getAccessRequestApprovedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ACCESSREQUESTAPPROVED_EVENT, transactionReceipt);
        ArrayList<AccessRequestApprovedEventResponse> responses = new ArrayList<AccessRequestApprovedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AccessRequestApprovedEventResponse typedResponse = new AccessRequestApprovedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.aadhaarHash = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.details = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static AccessRequestApprovedEventResponse getAccessRequestApprovedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(ACCESSREQUESTAPPROVED_EVENT, log);
        AccessRequestApprovedEventResponse typedResponse = new AccessRequestApprovedEventResponse();
        typedResponse.log = log;
        typedResponse.aadhaarHash = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.details = (String) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<AccessRequestApprovedEventResponse> accessRequestApprovedEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getAccessRequestApprovedEventFromLog(log));
    }

    public Flowable<AccessRequestApprovedEventResponse> accessRequestApprovedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ACCESSREQUESTAPPROVED_EVENT));
        return accessRequestApprovedEventFlowable(filter);
    }

    public static List<AccessRequestCreatedEventResponse> getAccessRequestCreatedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ACCESSREQUESTCREATED_EVENT, transactionReceipt);
        ArrayList<AccessRequestCreatedEventResponse> responses = new ArrayList<AccessRequestCreatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AccessRequestCreatedEventResponse typedResponse = new AccessRequestCreatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.aadhaarHash = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.details = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static AccessRequestCreatedEventResponse getAccessRequestCreatedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(ACCESSREQUESTCREATED_EVENT, log);
        AccessRequestCreatedEventResponse typedResponse = new AccessRequestCreatedEventResponse();
        typedResponse.log = log;
        typedResponse.aadhaarHash = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.details = (String) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<AccessRequestCreatedEventResponse> accessRequestCreatedEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getAccessRequestCreatedEventFromLog(log));
    }

    public Flowable<AccessRequestCreatedEventResponse> accessRequestCreatedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ACCESSREQUESTCREATED_EVENT));
        return accessRequestCreatedEventFlowable(filter);
    }

    public static List<AccessRevokedEventResponse> getAccessRevokedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ACCESSREVOKED_EVENT, transactionReceipt);
        ArrayList<AccessRevokedEventResponse> responses = new ArrayList<AccessRevokedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AccessRevokedEventResponse typedResponse = new AccessRevokedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.aadhaarHash = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.details = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static AccessRevokedEventResponse getAccessRevokedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(ACCESSREVOKED_EVENT, log);
        AccessRevokedEventResponse typedResponse = new AccessRevokedEventResponse();
        typedResponse.log = log;
        typedResponse.aadhaarHash = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.details = (String) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<AccessRevokedEventResponse> accessRevokedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getAccessRevokedEventFromLog(log));
    }

    public Flowable<AccessRevokedEventResponse> accessRevokedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ACCESSREVOKED_EVENT));
        return accessRevokedEventFlowable(filter);
    }

    public static List<BasicDataViewedEventResponse> getBasicDataViewedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(BASICDATAVIEWED_EVENT, transactionReceipt);
        ArrayList<BasicDataViewedEventResponse> responses = new ArrayList<BasicDataViewedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            BasicDataViewedEventResponse typedResponse = new BasicDataViewedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.aadhaarHash = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.details = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static BasicDataViewedEventResponse getBasicDataViewedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(BASICDATAVIEWED_EVENT, log);
        BasicDataViewedEventResponse typedResponse = new BasicDataViewedEventResponse();
        typedResponse.log = log;
        typedResponse.aadhaarHash = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.details = (String) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<BasicDataViewedEventResponse> basicDataViewedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getBasicDataViewedEventFromLog(log));
    }

    public Flowable<BasicDataViewedEventResponse> basicDataViewedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(BASICDATAVIEWED_EVENT));
        return basicDataViewedEventFlowable(filter);
    }

    public static List<DataEditedForUserEventResponse> getDataEditedForUserEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(DATAEDITEDFORUSER_EVENT, transactionReceipt);
        ArrayList<DataEditedForUserEventResponse> responses = new ArrayList<DataEditedForUserEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            DataEditedForUserEventResponse typedResponse = new DataEditedForUserEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.aadhaarHash = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.details = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static DataEditedForUserEventResponse getDataEditedForUserEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(DATAEDITEDFORUSER_EVENT, log);
        DataEditedForUserEventResponse typedResponse = new DataEditedForUserEventResponse();
        typedResponse.log = log;
        typedResponse.aadhaarHash = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.details = (String) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<DataEditedForUserEventResponse> dataEditedForUserEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getDataEditedForUserEventFromLog(log));
    }

    public Flowable<DataEditedForUserEventResponse> dataEditedForUserEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DATAEDITEDFORUSER_EVENT));
        return dataEditedForUserEventFlowable(filter);
    }

    public static List<InstitutionRegisteredEventResponse> getInstitutionRegisteredEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(INSTITUTIONREGISTERED_EVENT, transactionReceipt);
        ArrayList<InstitutionRegisteredEventResponse> responses = new ArrayList<InstitutionRegisteredEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            InstitutionRegisteredEventResponse typedResponse = new InstitutionRegisteredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.registrationNumber = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.details = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static InstitutionRegisteredEventResponse getInstitutionRegisteredEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(INSTITUTIONREGISTERED_EVENT, log);
        InstitutionRegisteredEventResponse typedResponse = new InstitutionRegisteredEventResponse();
        typedResponse.log = log;
        typedResponse.registrationNumber = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.details = (String) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<InstitutionRegisteredEventResponse> institutionRegisteredEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getInstitutionRegisteredEventFromLog(log));
    }

    public Flowable<InstitutionRegisteredEventResponse> institutionRegisteredEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(INSTITUTIONREGISTERED_EVENT));
        return institutionRegisteredEventFlowable(filter);
    }

    public static List<UserAccountDeletedEventResponse> getUserAccountDeletedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(USERACCOUNTDELETED_EVENT, transactionReceipt);
        ArrayList<UserAccountDeletedEventResponse> responses = new ArrayList<UserAccountDeletedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            UserAccountDeletedEventResponse typedResponse = new UserAccountDeletedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.aadhaarHash = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.details = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static UserAccountDeletedEventResponse getUserAccountDeletedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(USERACCOUNTDELETED_EVENT, log);
        UserAccountDeletedEventResponse typedResponse = new UserAccountDeletedEventResponse();
        typedResponse.log = log;
        typedResponse.aadhaarHash = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.details = (String) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<UserAccountDeletedEventResponse> userAccountDeletedEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getUserAccountDeletedEventFromLog(log));
    }

    public Flowable<UserAccountDeletedEventResponse> userAccountDeletedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(USERACCOUNTDELETED_EVENT));
        return userAccountDeletedEventFlowable(filter);
    }

    public static List<UserInvitedEventResponse> getUserInvitedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(USERINVITED_EVENT, transactionReceipt);
        ArrayList<UserInvitedEventResponse> responses = new ArrayList<UserInvitedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            UserInvitedEventResponse typedResponse = new UserInvitedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.aadhaarHash = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.details = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static UserInvitedEventResponse getUserInvitedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(USERINVITED_EVENT, log);
        UserInvitedEventResponse typedResponse = new UserInvitedEventResponse();
        typedResponse.log = log;
        typedResponse.aadhaarHash = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.details = (String) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<UserInvitedEventResponse> userInvitedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getUserInvitedEventFromLog(log));
    }

    public Flowable<UserInvitedEventResponse> userInvitedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(USERINVITED_EVENT));
        return userInvitedEventFlowable(filter);
    }

    public static List<UserViewedRecordsEventResponse> getUserViewedRecordsEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(USERVIEWEDRECORDS_EVENT, transactionReceipt);
        ArrayList<UserViewedRecordsEventResponse> responses = new ArrayList<UserViewedRecordsEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            UserViewedRecordsEventResponse typedResponse = new UserViewedRecordsEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.aadhaarHash = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.details = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static UserViewedRecordsEventResponse getUserViewedRecordsEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(USERVIEWEDRECORDS_EVENT, log);
        UserViewedRecordsEventResponse typedResponse = new UserViewedRecordsEventResponse();
        typedResponse.log = log;
        typedResponse.aadhaarHash = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.details = (String) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<UserViewedRecordsEventResponse> userViewedRecordsEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getUserViewedRecordsEventFromLog(log));
    }

    public Flowable<UserViewedRecordsEventResponse> userViewedRecordsEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(USERVIEWEDRECORDS_EVENT));
        return userViewedRecordsEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> logAccessRequestApproved(String aadhaarHash,
                                                                           String details) {
        final Function function = new Function(
                FUNC_LOGACCESSREQUESTAPPROVED,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(aadhaarHash),
                        new org.web3j.abi.datatypes.Utf8String(details)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> logAccessRequestCreated(String aadhaarHash,
                                                                          String details) {
        final Function function = new Function(
                FUNC_LOGACCESSREQUESTCREATED,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(aadhaarHash),
                        new org.web3j.abi.datatypes.Utf8String(details)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> logAccessRevoked(String aadhaarHash,
                                                                   String details) {
        final Function function = new Function(
                FUNC_LOGACCESSREVOKED,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(aadhaarHash),
                        new org.web3j.abi.datatypes.Utf8String(details)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> logBasicDataViewed(String aadhaarHash,
                                                                     String details) {
        final Function function = new Function(
                FUNC_LOGBASICDATAVIEWED,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(aadhaarHash),
                        new org.web3j.abi.datatypes.Utf8String(details)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> logDataEditedForUser(String aadhaarHash,
                                                                       String details) {
        final Function function = new Function(
                FUNC_LOGDATAEDITEDFORUSER,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(aadhaarHash),
                        new org.web3j.abi.datatypes.Utf8String(details)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> logInstitutionRegistered(
            String registrationNumber, String details) {
        final Function function = new Function(
                FUNC_LOGINSTITUTIONREGISTERED,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(registrationNumber),
                        new org.web3j.abi.datatypes.Utf8String(details)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> logUserAccountDeleted(String aadhaarHash,
                                                                        String details) {
        final Function function = new Function(
                FUNC_LOGUSERACCOUNTDELETED,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(aadhaarHash),
                        new org.web3j.abi.datatypes.Utf8String(details)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> logUserInvited(String aadhaarHash,
                                                                 String details) {
        final Function function = new Function(
                FUNC_LOGUSERINVITED,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(aadhaarHash),
                        new org.web3j.abi.datatypes.Utf8String(details)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> logUserViewedRecords(String aadhaarHash,
                                                                       String details) {
        final Function function = new Function(
                FUNC_LOGUSERVIEWEDRECORDS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(aadhaarHash),
                        new org.web3j.abi.datatypes.Utf8String(details)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static MedicalDataAccess load(String contractAddress, Web3j web3j,
                                         Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new MedicalDataAccess(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static MedicalDataAccess load(String contractAddress, Web3j web3j,
                                         TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new MedicalDataAccess(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static MedicalDataAccess load(String contractAddress, Web3j web3j,
                                         Credentials credentials, ContractGasProvider contractGasProvider) {
        return new MedicalDataAccess(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static MedicalDataAccess load(String contractAddress, Web3j web3j,
                                         TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new MedicalDataAccess(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<MedicalDataAccess> deploy(Web3j web3j, Credentials credentials,
                                                       ContractGasProvider contractGasProvider) {
        return deployRemoteCall(MedicalDataAccess.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<MedicalDataAccess> deploy(Web3j web3j, Credentials credentials,
                                                       BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(MedicalDataAccess.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    public static RemoteCall<MedicalDataAccess> deploy(Web3j web3j,
                                                       TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(MedicalDataAccess.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<MedicalDataAccess> deploy(Web3j web3j,
                                                       TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(MedicalDataAccess.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    public static void linkLibraries(List<Contract.LinkReference> references) {
        librariesLinkedBinary = linkBinaryWithReferences(BINARY, references);
    }

    private static String getDeploymentBinary() {
        if (librariesLinkedBinary != null) {
            return librariesLinkedBinary;
        } else {
            return BINARY;
        }
    }

    public static class AccessRequestApprovedEventResponse extends BaseEventResponse {
        public String aadhaarHash;

        public String details;
    }

    public static class AccessRequestCreatedEventResponse extends BaseEventResponse {
        public String aadhaarHash;

        public String details;
    }

    public static class AccessRevokedEventResponse extends BaseEventResponse {
        public String aadhaarHash;

        public String details;
    }

    public static class BasicDataViewedEventResponse extends BaseEventResponse {
        public String aadhaarHash;

        public String details;
    }

    public static class DataEditedForUserEventResponse extends BaseEventResponse {
        public String aadhaarHash;

        public String details;
    }

    public static class InstitutionRegisteredEventResponse extends BaseEventResponse {
        public String registrationNumber;

        public String details;
    }

    public static class UserAccountDeletedEventResponse extends BaseEventResponse {
        public String aadhaarHash;

        public String details;
    }

    public static class UserInvitedEventResponse extends BaseEventResponse {
        public String aadhaarHash;

        public String details;
    }

    public static class UserViewedRecordsEventResponse extends BaseEventResponse {
        public String aadhaarHash;

        public String details;
    }
}
