# Admin API Chart

## Prerequisites Details
* Kubernetes 1.5 or higher with alpha APIs enabled
* StatefulSets are in beta in 1.5
* Init containers are in beta 1.5
* PV support for underlying infrastructure

## StatefulSet Details
* https://kubernetes.io/docs/concepts/abstractions/controllers/statefulsets/

## StatefulSet Caveats
* https://kubernetes.io/docs/concepts/abstractions/controllers/statefulsets/#limitations

## Installing the Chart

To install the chart with release name `my-release`:

```console
$ helm install --name my-release admin-api
```
