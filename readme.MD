## Install knative cluster on mac
kn quickstart kind

# Local build

`cd mandelbrot`
`./mvnw spring-boot:build-image`

Result: 
[INFO] Successfully built image 'docker.io/library/mandelbrot:1.0.0-SNAPSHOT'

docker tag mandelbrot:1.0.0-SNAPSHOT dev.local/mandelbrot:1.0.0
docker tag images:1.0.0-SNAPSHOT dev.local/images:1.0.0

kind load --name "knative" docker-image dev.local/mandelbrot:1.0.0
kind load --name "knative" docker-image dev.local/images:1.0.0

kubectl apply -f mandelbrot/service.yaml
kubectl apply -f images/service.yaml


kind export logs --name knative



sdk install java 22.3.2.r17-grl
./mvnw spring-boot:build-image -Pnative

## Load tests
hey -n 100 -c 10 -m POST -T image/png -D mandelbrot/src/test/java/io/unstream/fractal/mandelbrot/fractal100.json http://fractal-faas-mandelbrot.default.127.0.0.1.sslip.io/createFractal

## Deployment to Google Cloud-Run

docker tag mandelbrot:1.0.0-SNAPSHOT europe-west3-docker.pkg.dev/fractal-functions/images/mandelbrot:latest
docker tag images:1.0.0-SNAPSHOT europe-west3-docker.pkg.dev/fractal-functions/images/images:latest

docker push europe-west3-docker.pkg.dev/fractal-functions/images/mandelbrot:latest
docker push europe-west3-docker.pkg.dev/fractal-functions/images/images:latest

gcloud run services replace mandelbrot/service_gcr.yaml
gcloud run services replace images/service_gcr.yaml

You need to authenticate against Cloud-Run. Get the Bearer Token by calling
`gcloud auth print-identity-token`.

hey -n 10 -c 1 -m POST -T image/png -D mandelbrot/src/test/java/io/unstream/fractal/mandelbrot/fractal1000.json -H "Authorization: Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6Ijk5YmNiMDY5MzQwYTNmMTQ3NDYyNzk0ZGZlZmE3NWU3OTk2MTM2MzQiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiIzMjU1NTk0MDU1OS5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsImF1ZCI6IjMyNTU1OTQwNTU5LmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwic3ViIjoiMTExODQ5NzUzNDU3MDEwNTAzMjc3IiwiZW1haWwiOiJpbmdvLndlaWNoc2VsQGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJhdF9oYXNoIjoic2pMWDhsTy01UkFVdk4za191VVU1ZyIsImlhdCI6MTY4ODQyNTY3OSwiZXhwIjoxNjg4NDI5Mjc5fQ.DL1V_-_-Yp51BTyhIB5NC4KaABDyHVNxB-l0zGxsqZPoi3pqImA6Vv3iGEeQ7OVUivDC-0jR1D2C3CMPsGTkIIpdJB4iMNDI79vq1bvZfW0b76Ym2FqEZ4_tvmvO8ZDxR9jHG2bjY58YwH7ogYnNf1zPw1oYTdMqawwYChEcjRhsJYrdzuz6KYcukx099BfML-MHQush7npujWKkFUqNgiEU-gkift6x48c4GUY9zpNZ6cpwBF5uc0osqvnlh2O5N_D6Y-rspiv097vKMBe6c_TrOOq-Y4yuPNnvFdxbDUwjmUD40YRbm02Vw6Zvwh7K2b1q6zc9V84RaGe806gQ3w" https://fractal-faas-mandelbrot-caskxmpsyq-ey.a.run.app/createFractal
hey -n 10 -c 1 -m POST -T image/png -D mandelbrot/src/test/java/io/unstream/fractal/mandelbrot/fractal1000.json https://localhost:8080/createFractal

hey -n 10 -c 1 -m POST -T image/png -D mandelbrot/src/test/java/io/unstream/fractal/mandelbrot/fractal1000.json http://fractal-faas-mandelbrot.default.127.0.0.1.sslip.io/createFractal

Go-App:
hey -n 1000 -c 10 -m GET -T image/png -D http://localhost:8080/api/v1/mandelbrot?max_iterations=1000&w=1000&h=1000
http://localhost:8080/api/v1/mandelbrot?c0=-0.8665014418863999&c0i=-0.24407616104639998&c1=-0.8640678719007999&c1i=-0.24164259106080002&max_iterations=10000&w=1000&h=1000
