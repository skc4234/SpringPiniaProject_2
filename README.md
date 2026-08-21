# SpringPiniaProject_2
> **Docker를 활용해 이미지를 빌드하고, Kubernetes(Minikube)에 배포하는 실습**

---
## Kubernetes (Minikube) 실습

> Ubuntu 환경에서 Docker 및 Minikube를 활용해 컨테이너 이미지 빌드 및 배포 연습

### 1. Docker 이미지 빌드 및 DockerHub push

```
# 1. Docker 이미지 빌드
docker build -t <image명> .

# 2. DockerHub 로그인
docker login -u <dockerhub_id>

# 3. DockerHub에 올리기 위한 이미지 태그 생성
docker tag <image명> <dockerhub_id>/<image명>

# 4. DockerHub로 이미지 push
docker push <dockerhub_id>/<image명>

# 저장소의 이미지 pull
docker pull <dockerhub_id>/<image명>
```

### 2. Minikube 실행 및 배포
```
# 1. Docker 드라이버 기반으로 Minikube 실행
minikube start --driver=docker

# 2. deployment.yaml 파일 적용 (배포 실행)
kubectl apply -f ~/k8s/deployment.yaml

# 3. 생성된 Pod 상태 확인
kubectl get pods

# Pod Status가 'Running'이 아닐 경우 아래 명령어로 로그 확인
kubectl logs <pod명>

# 실행중인 Service 확인
kubectl get svc

# Service 실행 및 웹 접속 => URL 생성
minikube service <service명>
```
