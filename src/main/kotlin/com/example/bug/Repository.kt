package com.example.bug

import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface Repository : ReactiveMongoRepository<Entity, String>
