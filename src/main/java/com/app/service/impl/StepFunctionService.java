package com.app.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.DisposableBean;
import software.amazon.awssdk.services.sfn.SfnClient;
import software.amazon.awssdk.services.sfn.model.SfnException;
import software.amazon.awssdk.services.sfn.model.StartExecutionRequest;
import software.amazon.awssdk.services.sfn.model.StartExecutionResponse;

@Service
public class StepFunctionService implements DisposableBean {

    @Value("${aws.step-function.arn}")
    private String stepFunctionArn;

    private final SfnClient stepFunctionsClient;


    public StepFunctionService(SfnClient stepFunctionsClient) {
        this.stepFunctionsClient = stepFunctionsClient;
    }

    public String startStepFunction(String input) {
        try {
            StartExecutionRequest request = StartExecutionRequest.builder()
                    .stateMachineArn(stepFunctionArn)
                    .input(input)
                    .build();

            StartExecutionResponse response = stepFunctionsClient.startExecution(request);

            return response.executionArn();
        } catch (SfnException e) {
            throw new RuntimeException("Failed to initiate Step Function: " + e.awsErrorDetails().errorMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initiate Step Function: " + e.getMessage(), e);
        }
    }

    @Override
    public void destroy() {
        if (stepFunctionsClient != null) {
            stepFunctionsClient.close();
        }
    }
}
