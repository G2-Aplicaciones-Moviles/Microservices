#!/bin/bash
set -e

# Create application user for chat service
mongosh --authenticationDatabase admin --host localhost -u $MONGO_INITDB_ROOT_USERNAME -p $MONGO_INITDB_ROOT_PASSWORD <<EOF
use $MONGO_INITDB_DATABASE
db.createUser({
  user: '$MONGO_APP_USER',
  pwd: '$MONGO_APP_PASSWORD',
  roles: [
    {
      role: 'readWrite',
      db: '$MONGO_INITDB_DATABASE'
    }
  ]
})
EOF

echo "MongoDB user created successfully"

