package dev.honegger.converters

import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import org.jooq.Converter
import java.time.LocalDateTime as JavaLocalDateTime
import kotlinx.datetime.LocalDateTime as KotlinLocalDateTime

internal class LocalDateTimeConverter : Converter<JavaLocalDateTime, KotlinLocalDateTime> {
    override fun from(databaseObject: JavaLocalDateTime?): KotlinLocalDateTime? = databaseObject?.toKotlinLocalDateTime()

    override fun to(userObject: KotlinLocalDateTime?): JavaLocalDateTime? = userObject?.toJavaLocalDateTime()

    override fun fromType() = JavaLocalDateTime::class.java

    override fun toType() = KotlinLocalDateTime::class.java
}
