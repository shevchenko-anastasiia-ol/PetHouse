package shevchenko.grpc;

import io.quarkus.grpc.GrpcService;
import jakarta.inject.Inject;
import io.grpc.stub.StreamObserver;
import shevchenko.Animal;
import shevchenko.AnimalService;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class AnimalGrpcService extends AnimalServiceGrpc.AnimalServiceImplBase {

    @Inject
    AnimalService service;

    @Override
    public void getAllAnimals(Empty request, StreamObserver<AnimalsList> responseObserver) {
        List<AnimalResponse> list = service.getAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        AnimalsList response = AnimalsList.newBuilder()
                .addAllAnimals(list)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getAnimalById(AnimalRequest request, StreamObserver<AnimalResponse> responseObserver) {
        service.getById(request.getId()).ifPresentOrElse(
                animal -> {
                    responseObserver.onNext(toResponse(animal));
                    responseObserver.onCompleted();
                },
                () -> responseObserver.onError(new RuntimeException("Animal not found"))
        );
    }

    @Override
    public void adoptAnimal(AdoptRequest request, StreamObserver<AdoptResponse> responseObserver) {
        try {
            Animal animal = service.markAsAdopted(request.getId());
            responseObserver.onNext(
                    AdoptResponse.newBuilder()
                            .setSuccess(true)
                            .setMessage("Adopted successfully")
                            .setAnimal(toResponse(animal))
                            .build()
            );
            responseObserver.onCompleted();
        } catch (RuntimeException e) {
            boolean notFound = e.getMessage().equals("Animal not found");
            responseObserver.onNext(
                    AdoptResponse.newBuilder()
                            .setSuccess(false)
                            .setMessage(e.getMessage())
                            .build()
            );
            responseObserver.onCompleted();
        }
    }

    @Override
    public void updateHealthStatus(UpdateHealthRequest request, StreamObserver<UpdateHealthResponse> responseObserver) {
        try {
            Animal animal = service.updateHealthStatus(request.getId(), request.getHealthStatus());
            responseObserver.onNext(
                    UpdateHealthResponse.newBuilder()
                            .setSuccess(true)
                            .setMessage("Health status updated")
                            .setAnimal(toResponse(animal))
                            .build()
            );
            responseObserver.onCompleted();
        } catch (RuntimeException e) {
            responseObserver.onNext(
                    UpdateHealthResponse.newBuilder()
                            .setSuccess(false)
                            .setMessage(e.getMessage())
                            .build()
            );
            responseObserver.onCompleted();
        }
    }

    private AnimalResponse toResponse(Animal animal) {
        return AnimalResponse.newBuilder()
                .setId(animal.id)
                .setName(animal.name)
                .setSpecies(animal.species)
                .setAge(animal.age)
                .setHealthStatus(animal.healthStatus)
                .setAdopted(animal.adopted)
                .build();
    }
}