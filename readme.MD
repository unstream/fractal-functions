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

hey -n 10 -c 1 -m POST -T image/png -D mandelbrot/src/test/java/io/unstream/fractal/mandelbrot/fractal1000.json -H "Authorization: Bearer <<ID-Token>>" https://fractal-faas-mandelbrot-caskxmpsyq-ey.a.run.app/createFractal
hey -n 10 -c 1 -m POST -T image/png -D mandelbrot/src/test/java/io/unstream/fractal/mandelbrot/fractal1000.json http://localhost:8080/createFractal
hey -n 100 -c 10 -m POST -T image/png -D mandelbrot/src/test/java/io/unstream/fractal/mandelbrot/fractal1000.json http://localhost:8084/fastMandelbrot

hey -n 10 -c 1 -m POST -T image/png -D mandelbrot/src/test/java/io/unstream/fractal/mandelbrot/fractal1000.json http://fractal-faas-mandelbrot.default.127.0.0.1.sslip.io/createFractal

hey -n 1000 -c 100 -m POST -T image/png -D mandelbrot/src/test/java/io/unstream/fractal/mandelbrot/fractal100.json https://fractal-gateway-8191wl1t.nw.gateway.dev/fastMandelbrot?key=<API-Key>

hey -n 1 -c 1 -m POST -T image/png -H "Authorization: Bearer <<ID-Token>>" -D mandelbrot/src/test/java/io/unstream/fractal/mandelbrot/fractal100.json https://fractal-faas-mandelbrot-caskxmpsyq-ey.a.run.app/mandelbrotPng



xk6:
https://k6.io/blog/ways-to-visualize-k6-results/
./k6 run --out dashboard -e API_KEY=<API-Key> k6-test.js

Go-App:
hey -n 100 -c 10 -m GET -T image/png "http://localhost:8081/api/v1/mandelbrot?max_iterations=1000&w=1000&h=1000"


http://localhost:8080/api/v1/mandelbrot?c0=-0.8665014418863999&c0i=-0.24407616104639998&c1=-0.8640678719007999&c1i=-0.24164259106080002&max_iterations=10000&w=1000&h=1000
