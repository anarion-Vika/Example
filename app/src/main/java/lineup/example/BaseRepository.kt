package lineup.example

abstract class BaseRepository {
    companion object {
        suspend fun <T : Any> handleRequest(requestFunc: suspend () -> T): APIResult<T> {
            return try {
                Success(requestFunc.invoke())
            } catch (he: HttpException) {
                Timber.e(he)
                HTTPError(he)
            } catch (e: Throwable) {
                Timber.e(e)
                Error(e)
            }
        }

        suspend fun <T : Any?> handleNullableRequest(requestFunc: suspend () -> T): APIResult<T?> {
            return try {
                SuccessNullable(requestFunc.invoke())
            } catch (he: HttpException) {
                Timber.e(he)
                HTTPError(he)
            } catch (e: Throwable) {
                Timber.e(e)
                Error(e)
            }
        }
    }
}