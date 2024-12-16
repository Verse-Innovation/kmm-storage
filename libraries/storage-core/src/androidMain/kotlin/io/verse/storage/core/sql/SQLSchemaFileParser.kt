@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.storage.core.sql

import android.util.Log
import io.tagd.langx.Context
import io.tagd.langx.IOException
import java.io.BufferedReader
import java.io.InputStreamReader

actual class SQLSchemaFileParser {

    actual fun parseSqlStatements(
        context: Context,
        schemaFiles: List<String>,
    ): List<String> {

        val statements = arrayListOf<String>()
        for (schemaFile in schemaFiles) {
            val fileStatements = parseAssetSQLSchemaFile(context, schemaFile)
            statements.addAll(fileStatements)
        }
        return statements
    }

    private fun parseAssetSQLSchemaFile(
        context: Context,
        schemaFile: String,
    ): List<String> {

        val statements: List<String> = parseAssetSQLSchemaFile(
            context = context,
            fileName = schemaFile
        )

        for (statement in statements) {
            Log.d(
                "SQLParser",
                "executing schema... -> $statement"
            )
        }

        return statements
    }

    private fun parseAssetSQLSchemaFile(
        context: Context,
        filePath: String? = "sql",
        fileName: String
    ): List<String> {

        var path = filePath
        val assetManager = context.assets

        return try {
            if (path != null && !path.endsWith("/")) {
                path = "$path/"
            }

            val `in` = assetManager.open(path + fileName)
            val isr = InputStreamReader(`in`)
            val br = BufferedReader(isr)

            var line: String?
            val builder = StringBuilder()

            while (br.readLine().also { line = it } != null) {
                if (line!!.trim { it <= ' ' }.isNotEmpty()) { // Remove new lines
                    builder.append(line)
                }
            }

            val statements = builder.toString()
                .split(";".toRegex())
                .dropLastWhile { it.isEmpty() }

            statements
        } catch (e: IOException) {
            e.printStackTrace()
            listOf()
        }
    }
}