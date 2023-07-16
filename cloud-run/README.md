gcloud api-gateway apis create images-api --project=fractal-functions

gcloud api-gateway api-configs create images-api-config --api=images-api --openapi-spec=images-api2.yaml --project=fractal-functions
gcloud api-gateway api-configs create mandelbrot-api-config --api=mandelbrot-api --openapi-spec=mandelbrot-api2.yaml --project=fractal-functions


gcloud api-gateway gateways create fractal-gateway \
--api=images-api --api-config=images-api-config \
--location=europe-west2 --project=fractal-functions


gcloud api-gateway gateways create fractal-gateway --api=images-api --api-config=images-api-config --location=europe-west2 --project=fractal-functions
gcloud api-gateway gateways create fractal-gateway --api=mandelbrot-api --api-config=mandelbrot-api-config --location=europe-west2 --project=fractal-functions


gcloud api-gateway gateways describe fractal-gateway   --location=europe-west2 --project=fractal-functions

## Enable API-Key Security in API-Gateway
gcloud api-gateway apis list

gcloud services enable <MANAGED_SERVICE>
