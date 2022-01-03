# dbstat_helidon
A simple java and javascript application written with Helidon (REST services) and JET (JavaScript UI).
The application connects to an Oracle Database and reads and displays the database's V$parameters view.
Privileged Database access (sysdba role, sys or system user) is required.

This application can be used to testify the OracleCMP Operator fpr Kubernetes, but also to showcase the difference between a plain Kubernetes Deployment and a Verrazzano Deployment.
There are two Deployments available: 
One Deployment for a typical Kubernetes Deployment which defines a Secret, a Service and a Container/POD Deployment.
The other Deployment defines a Verrazzano Application which includes Logging with ElasticSearch, SSL Access to a Load Balancer using Istio and Keycloak, Monitoring with Prometheus/Grafana.

To install the application:
1) the plain, regular way
2) the "Verrazzano" way
