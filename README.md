# dbstat_helidon
A simple java and javascript application written with Helidon (REST services) and JET (JavaScript UI).
The application connects to an Oracle Database and reads and displays the database's V$parameters view.
Privileged Database access (sysdba role, sys or system user) is required.

This application can be used to testify the OracleCMP Operator for Kubernetes (https://github.com/ilfur/k8s_operator_cloudmgmtpack), but also to showcase the difference between a plain Kubernetes Deployment and a Verrazzano Deployment.
There are two Deployments available for this application: 
One Deployment for a typical Kubernetes Cluster which defines a Secret, a Service and a Container/POD Deployment.
The other Deployment defines a Verrazzano Application (a plain Kubernetes Cluster with Verrazzano installation required) which includes Logging with ElasticSearch, SSL Access to a Load Balancer using Istio and Keycloak, Monitoring with Prometheus/Grafana, Network Visualisation with Istio and Kiali.

To install the application:
1) In the plain, regular way
* clone the project to You local drive and hop into the "helidon-db-status" subdirectory
git clone https://ilfur/dbstat_helidon
cd helidon-db-status

* In a regular Kubernetes Cluster, create a new namespace, say "dbstat-helidon"
kubectl create namespace dbstat-helidon

* In that namespace, create a new Secret to access an existing Oracle Database as user "system"
kubectl create secret generic db-user-pass \
   --from-literal=pdb_admin=system \
   --from-literal=pdb_pwd='mypassword' \
   --from-literal=pdb_conn='192.168.10.123:1521/mypdb' \
   -n dbstat-helidon
   
* In that same namespace, create a new ConfigMap holding the Connect String to the Oracle Database.
This is redundant to the previously created secret, but a good test, showcase and demo to do so.
kubectl apply -f configmap.yaml

* Now, do the deployment of the Container itself into that same namespace and register the network service to access the Container through port 8080. There is no need to compile the recently cloned project, since the Container is pre-built and available on docker.io
kubectl apply -f app.yaml

* When the Deployment is up and running, i.e. Pod is downloaded and started correctly, try to access the application. You should see a start screen with the Connection Information pointing to Your database or whatever is specified in the prior secret and the configmap.
![First Screen](scrn_3.jpg)


3) the "Verrazzano" way
