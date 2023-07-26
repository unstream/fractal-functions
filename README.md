# Serverless Architectures with Spring Cloud Function and Knative
Project showcasing developing an app to visualize the Mandelbrot set with a Function-as-s-Service architecture.

## Cheat cheat
The following commands assume development on Mac OS.
### Install knative on Mac OS
```
kn quickstart kind
```
### Build the application locally
```
cd mandelbrot
mvn spring-boot:build-image
```

### Switch to correct JVM and build a native image with graalvm
```
sdk use java 22.3.2.r17-grl
mvn -Pnative native:compile
```

### tag image and upload to registry in kind, install service with function
```
docker tag mandelbrot:1.0.0-SNAPSHOT dev.local/mandelbrot:1.0.0
kind load --name "knative" docker-image dev.local/mandelbrot:1.0.0
kubectl apply -f service.yaml
```

### Export logs from kind
```
kind export logs --name knative
```

## Run local load tests with hey
```
cd e2e
hey -n 100 -c 10 -m POST -t 0 -T image/png -D fractal100.json \
http://fractal-faas-mandelbrot.default.127.0.0.1.sslip.io/createFractal
```


### Deploy the service to Google Cloud-Run
```
cd cloud-run
docker tag mandelbrot:1.0.0-SNAPSHOT europe-west3-docker.pkg.dev/fractal-functions/images/mandelbrot:LATEST
docker push europe-west3-docker.pkg.dev/fractal-functions/images/mandelbrot:LATEST
gcloud run services replace service_gcr.yaml
```

### Get an identity token to access the Google Cloud Run Ingress
`gcloud auth print-identity-token`.


### Test with xk6

Infos about xk6 can be found at https://k6.io/blog/ways-to-visualize-k6-results/
```
cd e2e
./k6 run --out dashboard -e API_KEY=$API_KEY k6-test-ramp.js
```


