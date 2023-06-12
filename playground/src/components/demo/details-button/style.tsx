import styled from 'styled-components';
import {Text, View} from 'react-native';

export const Container = styled(View)`
  display: flex;
  flex-direction: column;
  height: 400px;
`;

export const ModalContainer = styled(View)`
  padding: 12px;
  display: flex;
  flex-direction: column;
`;

export const PropertiesContainer = styled(View)`
  padding-top: 12px;
  gap: 6px;
`;

export const PropertyWrapper = styled(View)`
  flex-direction: row;
  align-items: center;
  gap: 6px;
`;

export const PropertyName = styled(Text)`
  color: #000;
  font-size: 14px;
  font-weight: 400;
`;

export const PropertyValue = styled(Text)`
  color: #000;
  font-size: 14px;
  font-weight: 700;
`;

export const ButtonGroup = styled(View)`
  flex-direction: row;
  gap: 6px;
  margin-top: 6px;
`;
