package org.musiccollection.repository

import org.musiccollection.repository.entity.ArtistEntity
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class ArtistRepository : BaseRepository<ArtistEntity>
