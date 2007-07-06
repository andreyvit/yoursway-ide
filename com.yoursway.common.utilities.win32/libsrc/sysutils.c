
#include <windows.h>
#include <tchar.h>
#include <winnt.h>
#include <shlobj.h>

#include "sysutils.h"

BOOL APIENTRY DllMain( HANDLE hModule, 
                       DWORD  ul_reason_for_call, 
                       LPVOID lpReserved
					 )
{
    return TRUE;
}

enum { ASSERT_UNICODE_BUILD = 2 / (sizeof(TCHAR) == 2) };

jstring JNICALL getSpecialLocation(JNIEnv *env, jobject object, DWORD locationId)
{
	TCHAR pszPath[MAX_PATH];

	if ( SHGetSpecialFolderPath(NULL, pszPath, locationId, TRUE) ) {
		return (*env)->NewString( env, pszPath, _tcslen(pszPath) );
	} else {
		return NULL;
	}
}
JNIEXPORT jstring JNICALL Java_com_yoursway_utils_SystemUtilitiesImpl_getMyDocumentsLocation0
  (JNIEnv *env, jobject object)
{
	return getSpecialLocation(env, object, CSIDL_PERSONAL);
}

JNIEXPORT jstring JNICALL Java_com_yoursway_utils_SystemUtilitiesImpl_getProgramFilesLocation0
  (JNIEnv *env, jobject object)
{
	return getSpecialLocation(env, object, CSIDL_PERSONAL);
}

JNIEXPORT jstring JNICALL Java_com_yoursway_utils_SystemUtilitiesImpl_getApplicationDataLocation0
  (JNIEnv *env, jobject object)
{
	return getSpecialLocation(env, object, CSIDL_PROGRAM_FILES);
}


JNIEXPORT jboolean JNICALL Java_com_yoursway_utils_SystemUtilitiesImpl_isConsoleApplication
  (JNIEnv *env, jobject object, jstring filenamejs)
{
	LPCTSTR filename = L"TEST";
	HANDLE hFile;
	HANDLE hFileMapping;
	LPVOID lpFileBase;
	jboolean result = 0;
	PIMAGE_DOS_HEADER dosHeader;
	hFile = CreateFile(filename, GENERIC_READ, FILE_SHARE_READ, NULL,
		OPEN_EXISTING, FILE_ATTRIBUTE_NORMAL, 0);
	if ( hFile == INVALID_HANDLE_VALUE ) {
		printf("Couldn't open file with CreateFile()\n");
		return 0;
	}
	hFileMapping = CreateFileMapping(hFile, NULL, PAGE_READONLY, 0, 0, NULL);
	if ( hFileMapping == 0 ) {
		CloseHandle(hFile);
		printf("Couldn't open file mapping with CreateFileMapping()\n");
		return 0;
	}
	lpFileBase = MapViewOfFile(hFileMapping, FILE_MAP_READ, 0, 0, 0);
	if ( lpFileBase == 0 ) {
		CloseHandle(hFileMapping);
		CloseHandle(hFile);
		printf("Couldn't map view of file with MapViewOfFile()\n");
		return 0;
	}
	printf("Dump of file %s\n\n", filename);
	dosHeader = (PIMAGE_DOS_HEADER)lpFileBase;
	if ( dosHeader->e_magic == IMAGE_DOS_SIGNATURE ) {
		IMAGE_NT_HEADERS *pNTHeader = (IMAGE_NT_HEADERS *) ((char *) dosHeader + dosHeader->e_lfanew);
		WORD subsys = pNTHeader->OptionalHeader.Subsystem;
		if (subsys == IMAGE_SUBSYSTEM_WINDOWS_CUI)
			result = 1;
	} else {
	}
	UnmapViewOfFile(lpFileBase);
	CloseHandle(hFileMapping);
	CloseHandle(hFile);
	return result;
}

