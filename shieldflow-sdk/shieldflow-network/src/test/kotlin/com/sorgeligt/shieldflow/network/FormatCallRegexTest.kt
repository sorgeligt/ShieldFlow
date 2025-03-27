package com.sorgeligt.shieldflow.network

import com.sorgeligt.shieldflow.network.measure_events.cleanAndFormatCallName
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class FormatCallRegexTest {

    @Test
    fun fullFormat() {
        val path =
            "some/url/414124/sum/398ba14d-b254-4755-9a37-e982c696497e/hi/${UUID.randomUUID()}/21321"
        assertEquals("some/url/{id}/sum/{uuid}/hi/{uuid}/{id}", cleanAndFormatCallName(path))
    }

    @Test
    fun uuidFormat() {
        val randomUUID = UUID.randomUUID()
        val lowercaseUUID = "ca761232-ed42-11ce-bacd-00aa0057b223"
        val uppercaseUUID = "CA761232-ED42-11CE-BACD-00AA0057B223"
        val uuidWithoutDashes = "ca761232ed4211cebacd00aa0057b223"
        val invalidUUID1 = uuidWithoutDashes.drop(1)
        val invalidUUID2 = lowercaseUUID.drop(1)
        val invalidUUID3 = uuidWithoutDashes + "2"
        val invalidUUID4 = lowercaseUUID + "2"
        val invalidPath = "/$invalidUUID1/$invalidUUID2/$invalidUUID3/$invalidUUID4"
        val validPath = "/$randomUUID/$lowercaseUUID/$uppercaseUUID/$uuidWithoutDashes"
        val path = "some/url$validPath${invalidPath}/all"
        assertEquals(
            expected = "some/url/{uuid}/{uuid}/{uuid}/{uuid}${invalidPath}/all",
            actual = cleanAndFormatCallName(path)
        )
    }

    @Test
    fun idFormat() {
        val path = "some/url/414124a/133-h/3333/123123/1-23/231/124412/421/1/all"
        assertEquals(
            expected = "some/url/414124a/133-h/{id}/{id}/1-23/{id}/{id}/{id}/{id}/all",
            actual = cleanAndFormatCallName(path)
        )
    }

    @Test
    fun noPatternFormat() {
        val path = "some/url/faejhjahf/wfkjakjawf/wafj1rjk1r2kl/f12ijijfr12/"
        assertEquals(path, cleanAndFormatCallName(path))
    }
}
