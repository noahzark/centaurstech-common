//
//  AESCipher.m
//  AESCipher
//
//  Created by Welkin Xie on 8/13/16.
//  Copyright © 2016 WelkinXie. All rights reserved.
//
//  Patched by Feliciano Long on 8/21/2018.
//
//  https://github.com/WelkinXie/AESCipher-iOS
//

#import "AESCipher.h"
#import <CommonCrypto/CommonCryptor.h>

NSString const *kInitVector = @"A-16-Byte-String";
size_t const kKeySize = kCCKeySizeAES128;

NSData * cipherOperation(NSData *contentData, NSData *keyData, CCOperation operation, NSData *ivData) {
    NSUInteger dataLength = contentData.length;
    
    void const *initVectorBytes = ivData.bytes;
    void const *contentBytes = contentData.bytes;
    void const *keyBytes = keyData.bytes;
    
    size_t operationSize = dataLength + kCCBlockSizeAES128;
    void *operationBytes = malloc(operationSize);
    if (operationBytes == NULL) {
        return nil;
    }
    size_t actualOutSize = 0;
    
    CCCryptorStatus cryptStatus = CCCrypt(operation,
                                          kCCAlgorithmAES,
                                          kCCOptionPKCS7Padding,
                                          keyBytes,
                                          kKeySize,
                                          initVectorBytes,
                                          contentBytes,
                                          dataLength,
                                          operationBytes,
                                          operationSize,
                                          &actualOutSize);
    
    if (cryptStatus == kCCSuccess) {
        return [NSData dataWithBytesNoCopy:operationBytes length:actualOutSize];
    }
    free(operationBytes);
    operationBytes = NULL;
    return nil;
}

NSString * aesEncryptString(NSString *content, NSString *key) {
    NSCParameterAssert(content);
    NSCParameterAssert(key);
    
    NSData *contentData = [content dataUsingEncoding:NSUTF8StringEncoding];
    NSData *keyData = [key dataUsingEncoding:NSUTF8StringEncoding];
    NSData *encrptedData = aesEncryptData(contentData, keyData);
    return [encrptedData base64EncodedStringWithOptions:NSDataBase64EncodingEndLineWithLineFeed];
}

NSString * aesEncryptStringWithIV(NSString *content, NSString *key, NSString *iv) {
    NSCParameterAssert(content);
    NSCParameterAssert(key);
    NSCParameterAssert(iv);
    
    NSData *contentData = [content dataUsingEncoding:NSUTF8StringEncoding];
    NSData *keyData = [[NSData alloc] initWithBase64EncodedString:key options:NSDataBase64DecodingIgnoreUnknownCharacters];
    NSData *ivData = [[NSData alloc] initWithBase64EncodedString:iv options:NSDataBase64DecodingIgnoreUnknownCharacters];
    
    NSData *encrptedData = aesEncryptDataWithIV(contentData, keyData, ivData);
    return [encrptedData base64EncodedStringWithOptions:NSDataBase64EncodingEndLineWithLineFeed];
}

NSString * aesDecryptString(NSString *content, NSString *key) {
    NSCParameterAssert(content);
    NSCParameterAssert(key);
    
    NSData *contentData = [[NSData alloc] initWithBase64EncodedString:content options:NSDataBase64DecodingIgnoreUnknownCharacters];
    NSData *keyData = [key dataUsingEncoding:NSUTF8StringEncoding];
    
    NSData *decryptedData = aesDecryptData(contentData, keyData);
    return [[NSString alloc] initWithData:decryptedData encoding:NSUTF8StringEncoding];
}

NSString * aesDecryptStringWithIV(NSString *content, NSString *key, NSString *iv) {
    NSCParameterAssert(content);
    NSCParameterAssert(key);
    NSCParameterAssert(iv);
    
    NSData *contentData = [[NSData alloc] initWithBase64EncodedString:content options:NSDataBase64DecodingIgnoreUnknownCharacters];
    NSData *keyData = [[NSData alloc] initWithBase64EncodedString:key options:NSDataBase64DecodingIgnoreUnknownCharacters];
    NSData *ivData = [[NSData alloc] initWithBase64EncodedString:iv options:NSDataBase64DecodingIgnoreUnknownCharacters];
    
    NSData *decryptedData = aesDecryptDataWithIV(contentData, keyData, ivData);
    return [[NSString alloc] initWithData:decryptedData encoding:NSUTF8StringEncoding];
}

NSData * aesEncryptData(NSData *contentData, NSData *keyData) {
    NSData *ivData = [kInitVector dataUsingEncoding:NSUTF8StringEncoding];
    return aesEncryptDataWithIV(contentData, keyData, ivData);
}

NSData * aesEncryptDataWithIV(NSData *contentData, NSData *keyData, NSData *ivData) {
    NSCParameterAssert(contentData);
    NSCParameterAssert(keyData);
    NSCParameterAssert(ivData);
    
    NSString *hint = [NSString stringWithFormat:@"The key size of AES-%lu should be %lu bytes! But got: %lu", kKeySize * 8, kKeySize, keyData.length];
    NSCAssert(keyData.length == kKeySize, hint);
    return cipherOperation(contentData, keyData, kCCEncrypt, ivData);
}

NSData * aesDecryptData(NSData *contentData, NSData *keyData) {
    NSData *ivData = [kInitVector dataUsingEncoding:NSUTF8StringEncoding];
    return aesDecryptDataWithIV(contentData, keyData, ivData);
}

NSData * aesDecryptDataWithIV(NSData *contentData, NSData *keyData, NSData *ivData) {
    NSCParameterAssert(contentData);
    NSCParameterAssert(keyData);
    NSCParameterAssert(ivData);
    
    NSString *hint = [NSString stringWithFormat:@"The key size of AES-%lu should be %lu bytes!", kKeySize * 8, kKeySize];
    NSCAssert(keyData.length == kKeySize, hint);
    return cipherOperation(contentData, keyData, kCCDecrypt, ivData);
}
