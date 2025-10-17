package shevchenko.grpc;

import io.quarkus.grpc.GrpcService;
import jakarta.inject.Inject;
import io.grpc.stub.StreamObserver;
import shevchenko.Animal;
import shevchenko.FakeAnimalRepository;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class AnimalGrpcService extends AnimalServiceGrpc.AnimalServiceImplBase {

    @Inject
    FakeAnimalRepository repository;

    @Override
    public void getAllAnimals(Empty request, StreamObserver<AnimalsList> responseObserver) {
        List<AnimalResponse> list = repository.findAll().stream()
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
        repository.findById(request.getId()).ifPresentOrElse(
                animal -> {
                    responseObserver.onNext(toResponse(animal));
                    responseObserver.onCompleted();
                },
                () -> responseObserver.onError(new RuntimeException("Animal not found"))
        );
    }

    @Override
    public void adoptAnimal(AdoptRequest request, StreamObserver<AdoptResponse> responseObserver) {
        repository.findById(request.getId()).ifPresentOrElse(
                animal -> {
                    if (animal.adopted) {
                        responseObserver.onNext(
                                AdoptResponse.newBuilder()
                                        .setSuccess(false)
                                        .setMessage("Already adopted")
                                        .build()
                        );
                    } else {
                        animal.adopted = true;
                        repository.update(animal);

                        responseObserver.onNext(
                                AdoptResponse.newBuilder()
                                        .setSuccess(true)
                                        .setMessage("Adopted successfully")
                                        .setAnimal(toResponse(animal))
                                        .build()
                        );
                    }
                    responseObserver.onCompleted();
                },
                () -> {
                    responseObserver.onNext(
                            AdoptResponse.newBuilder()
                                    .setSuccess(false)
                                    .setMessage("Animal not found")
                                    .build()
                    );
                    responseObserver.onCompleted();
                }
        );
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

    public Animal updateHealthStatus(Long id, String status) {
        return repository.updateHealthStatus(id, status);
    }
}
