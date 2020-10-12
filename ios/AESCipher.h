//
//  AESCipher.h
//  AESCipher
//
//  Created by Welkin Xie on 8/13/16.
//  Copyright © 2016 WelkinXie. All rights reserved.
//
//  Patched by Feliciano Long on 8/21/2018.
//
//  https://github.com/WelkinXie/AESCipher-iOS
//

#import <Foundation/Foundation.h>

NSString * aesEncryptString(NSString *content, NSString *key);
NSString * aesEncryptStringWithIV(NSString *content, NSString *key, NSString *iv);
NSString * aesDecryptString(NSString *content, NSString *key);
NSString * aesDecryptStringWithIV(NSString *content, NSString *key, NSString *iv);

NSData * aesEncryptData(NSData *data, NSData *key);
NSData * aesEncryptDataWithIV(NSData *data, NSData *key, NSData *ivData);
NSData * aesDecryptData(NSData *data, NSData *key);
NSData * aesDecryptDataWithIV(NSData *data, NSData *key, NSData *ivData);
