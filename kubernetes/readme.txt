### Preparing DOCKER images ###

cd config-server
docker build -t ivangorbunovv/config-server-order-processing-system-image .
docker tag ivangorbunovv/config-server-order-processing-system-image ivangorbunovv/order-processing-system:config-server
docker push ivangorbunovv/order-processing-system:config-server
cd ..

cd eureka-server
docker build -t ivangorbunovv/order-processing-system:eureka-server .
docker tag ivangorbunovv/eureka-server-order-processing-system-image ivangorbunovv/order-processing-system:eureka-server
docker push ivangorbunovv/order-processing-system:eureka-server
cd ..

cd api-gateway
docker build -t ivangorbunovv/api-gateway-order-processing-system-image .
docker tag ivangorbunovv/api-gateway-order-processing-system-image ivangorbunovv/order-processing-system:api-gateway
docker push ivangorbunovv/order-processing-system:api-gateway
cd ..

cd order-registrar-service
docker build -t ivangorbunovv/registrar-service-order-processing-system-image .
docker tag ivangorbunovv/registrar-service-order-processing-system-image ivangorbunovv/order-processing-system:registrar-service
docker push ivangorbunovv/order-processing-system:registrar-service
cd ..

cd order-demonstrator-service
docker build -t ivangorbunovv/demonstrator-service-order-processing-system-image .
docker tag ivangorbunovv/demonstrator-service-order-processing-system-image ivangorbunovv/order-processing-system:demonstrator-service
docker push ivangorbunovv/order-processing-system:demonstrator-service
cd ..


### Kubernetes operations ###

kubectl apply -f kubernetes\deploy-and-service-for-postgres.yaml
kubectl apply -f kubernetes\deploy-and-service-for-kafka.yaml
kubectl apply -f kubernetes\deploy-and-service-for-config-server.yaml
kubectl apply -f kubernetes\deploy-and-service-for-eureka-server.yaml
kubectl apply -f kubernetes\deploy-and-service-for-api-gateway.yaml
kubectl apply -f kubernetes\deploy-and-service-for-registrar-service.yaml
kubectl apply -f kubernetes\deploy-and-service-for-demonstrator-service.yaml
