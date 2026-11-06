import JSEncrypt from 'jsencrypt';

/**
 * 加密
 * @param {String} plaintext 需要加密的字符串
 * @param {String} publicKey 公钥
 * @returns {string|null} - 加密后的Base64编码字符串，失败时返回null
 */
export function encryptMessage(plaintext, publicKey = "MEgCQQCTAXoWEsFhKbfSY8XpK9/nRA47W9upkhODIUfYlcJC5MhnGG+2GafoB+/x\nlujUNPz0DSycQsRRVWDF1VlNMyT7AgMBAAE=") {
  const encrypt = new JSEncrypt();
  encrypt.setPublicKey(publicKey);
  return encrypt.encrypt(plaintext);
}

/**
 * 解密
 * @param {String} ciphertext  需要解密的字符串
 * @param {String} privateKey 私钥
 * @returns {string|null} - 解密后的原始文本，失败时返回null
 */

export function decryptMessage(ciphertext, privateKey='MIIBPAIBAAJBAJMBehYSwWEpt9Jjxekr3+dEDjtb26mSE4MhR9iVwkLkyGcYb7YZp+gH7/GW6NQ0/PQNLJxCxFFVYMXVWU0zJPsCAwEAAQJAK9kY7VSS0cR7zAlukuo/Ut/9zr7LenzTzR00LOoeVJAZN8+23enhYmlenDmcd44lf9n2BMcwwF/5qThTZpYeYQIjANMt8JRhUs2NwuIKRd86o1u5KosDx4AUcXLT7WIij0XjphcCHwCyNMYsQDhLs/OOSfXbQGPo/0rW0pY01r76O48Ear0CImBE7Qt3Ynv6SAErlbax0I6b1t0Ql/23VBB4tBqsjovZv3kCHwCKPWQpuZuyJiNeksM01OtsOHY+1VeOH7RAe2lDXOUCIjg+IpkGoJ0UY0NNKjwEcmjZKhyiYfY/R/zv3SoiHa9vldw=') {
  const decrypt = new JSEncrypt();
  decrypt.setPrivateKey(privateKey);
  return decrypt.decrypt(ciphertext);
}

export default {
  encryptMessage,
  decryptMessage,
}