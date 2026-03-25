# devskala-2

Vue 3 + Spring Boot 기반 Simple Todo App 모노레포입니다.  
로컬 개발부터 Docker 이미지 빌드, Harbor 푸시, EKS 배포까지 실습 가능한 구조입니다.

## 프로젝트 개요

- Backend: Todo REST API (`GET/POST/PATCH/DELETE`)
- Frontend: Vue 단일 페이지 Todo UI
- 배포: Docker + Kubernetes + GitHub Actions + Harbor + EKS
- Kubernetes namespace 고정: `skala3-ai1`

## 기술 스택

- Backend: Java 21, Spring Boot 3, Spring Data JPA, H2, Gradle
- Frontend: Vue 3, Vite, Axios, Nginx
- DevOps: Docker, Kubernetes, GitHub Actions, AWS EKS, Harbor

## 폴더 구조

```text
devskala-2/
├─ backend/
├─ frontend/
├─ k8s/
│  ├─ namespace.yaml
│  ├─ backend-deployment.yaml
│  ├─ backend-service.yaml
│  ├─ frontend-deployment.yaml
│  └─ frontend-service.yaml
├─ .github/
│  └─ workflows/
│     └─ ci-cd.yaml
├─ docker-compose.yml
└─ README.md
```

## 로컬 실행 방법

### Backend 실행

```bash
cd backend
./gradlew bootRun
```

- Health: `http://localhost:8080/`
- API: `http://localhost:8080/api/todos`

### Frontend 실행

```bash
cd frontend
npm install
cp .env.example .env
npm run dev
```

- App: `http://localhost:5173`
- 기본 API 주소: `VITE_API_BASE_URL=http://localhost:8080`

## Docker 빌드/실행 방법

프로젝트 루트(`devskala-2`)에서:

```bash
docker compose up --build -d
```

종료:

```bash
docker compose down
```

## Kubernetes 적용 방법

최초 1회 수동 배포:

```bash
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/backend-deployment.yaml
kubectl apply -f k8s/backend-service.yaml
kubectl apply -f k8s/frontend-deployment.yaml
kubectl apply -f k8s/frontend-service.yaml
```

확인:

```bash
kubectl get ns
kubectl get deploy -n skala3-ai1
kubectl get svc -n skala3-ai1
kubectl get pods -n skala3-ai1
```

## GitHub Actions 동작 흐름

`.github/workflows/ci-cd.yaml`

1. main push / PR / 수동 실행 트리거
2. backend build
3. frontend build
4. backend/frontend 이미지 각각 Harbor push (`<sha7>`, `latest`)
5. AWS OIDC role assume 후 EKS kubeconfig 갱신
6. `kubectl set image`로 `skala3-ai1` namespace의 deployment 이미지 업데이트
7. rollout status 확인

## GitHub Variables / Secrets 설정

Actions Variables:

- `AWS_REGION=ap-northeast-2`
- `EKS_CLUSTER_NAME=skala-2025`
- `K8S_NAMESPACE=skala3-ai1` (워크플로우 `env`와 동일하게 맞추기)
- `HARBOR_REGISTRY=<하버 주소>`
- `HARBOR_PROJECT=skala26a-ai1`
- `BACKEND_IMAGE_NAME=devskala2-backend`
- `FRONTEND_IMAGE_NAME=devskala2-frontend`
- `BACKEND_DEPLOYMENT_NAME=devskala2-backend`
- `FRONTEND_DEPLOYMENT_NAME=devskala2-frontend`
- `BACKEND_CONTAINER_NAME=backend`
- `FRONTEND_CONTAINER_NAME=frontend`
- `AWS_ROLE_TO_ASSUME=<AWS IAM Role ARN>`

Actions Secrets:

- `HARBOR_USERNAME=<하버 아이디>`
- `HARBOR_PASSWORD=<하버 비밀번호>`

## Harbor -> EKS 배포 구조

- GitHub Actions가 backend/frontend 이미지를 Harbor (`skala26a-ai1` project)로 push
- EKS의 deployment가 새 이미지 태그로 갱신
- frontend는 `LoadBalancer`, backend는 `ClusterIP` 서비스로 운영
