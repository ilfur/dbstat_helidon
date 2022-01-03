kubectl create namespace dbstat-helidon
kubectl create secret generic db-user-pass \
   --from-literal=pdb_admin=admin \
   --from-literal=pdb_pwd='mypassword' \
   --from-literal=pdb_conn='127.0.0.1:1521/mypdb' \
   -n dbstat-helidon